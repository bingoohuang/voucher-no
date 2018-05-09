package com.github.bingoohuang.voucherno;


/**
 * This program refers to the java-bloomfilter,you can get its details form https://github.com/MagnusS/Java-BloomFilter.
 * You have any questions about this program please put issues on github to
 * https://github.com/wxisme/bloomfilter
 */
public class BloomFilter {
    private final JedisCommandsBitSet bitSet;

    private final int bitSetSize;
    private final int k; // number of hash functions


    /**
     * Constructs an empty Bloom filter. The total length of the Bloom filter will be
     * c*n.
     *
     * @param c          is the number of bits used per element.
     * @param n          is the expected number of elements the filter will contain.
     * @param k          is the number of hash functions used.
     * @param filterData a BitSet representing an existing Bloom filter.
     */
    public BloomFilter(double c, int n, int k, JedisCommandsBitSet filterData) {
        this.k = k;
        this.bitSetSize = (int) Math.ceil(c * n);
        this.bitSet = filterData;
    }


    /**
     * Constructs an empty Bloom filter with a given false positive probability. The number of bits per
     * element and the number of hash functions is estimated
     * to match the false positive probability.
     *
     * @param falsePositiveProbability is the desired false positive probability.
     * @param expectedNumberOfElements is the expected number of elements in the Bloom filter.
     * @param filterData               a BitSet representing an existing Bloom filter.
     */
    public BloomFilter(double falsePositiveProbability, int expectedNumberOfElements, JedisCommandsBitSet filterData) {
        this(Math.ceil(-(Math.log(falsePositiveProbability) / Math.log(2.0))) / Math.log(2.0), // c = k / ln(2)
                expectedNumberOfElements,
                (int) Math.ceil(-(Math.log(falsePositiveProbability) / Math.log(2.0))), filterData); // k = ceil(-log_2(false prob.))
    }


    /**
     * Adds an object to the Bloom filter. The output from the object's
     * toString() method is used as input to the hash functions.
     *
     * @param element is an element to register in the Bloom filter.
     * @return added without conflicts or not.
     */
    public boolean add(String element) {
        return add(element.getBytes(MessageDigestUtils.UTF8));
    }

    /**
     * Adds an array of bytes to the Bloom filter.
     *
     * @param bytes array of bytes to add to the Bloom filter.
     * @return added without conflicts or not.
     */
    public boolean add(byte[] bytes) {
        int[] hashes = MessageDigestUtils.createHashes(bytes, k);
        int settedNum = 0;
        for (int hash : hashes) {
            if (bitSet.set(Math.abs(hash % bitSetSize), true)) settedNum++;
        }

        return settedNum < hashes.length;
    }

    /**
     * Returns true if the element could have been inserted into the Bloom filter.
     * Use getFalsePositiveProbability() to calculate the probability of this
     * being correct.
     *
     * @param element element to check.
     * @return true if the element could have been inserted into the Bloom filter.
     */
    public boolean contains(String element) {
        return contains(element.getBytes(MessageDigestUtils.UTF8));
    }

    /**
     * Returns true if the array of bytes could have been inserted into the Bloom filter.
     * Use getFalsePositiveProbability() to calculate the probability of this
     * being correct.
     *
     * @param bytes array of bytes to check.
     * @return true if the array could have been inserted into the Bloom filter.
     */
    public boolean contains(byte[] bytes) {
        int[] hashes = MessageDigestUtils.createHashes(bytes, k);
        for (int hash : hashes) {
            if (!bitSet.get(Math.abs(hash % bitSetSize))) return false;
        }
        return true;
    }


}