[![Build Status](https://travis-ci.org/bingoohuang/voucher-no.svg?branch=master)](https://travis-ci.org/bingoohuang/voucher-no)
[![Coverage Status](https://coveralls.io/repos/github/bingoohuang/voucher-no/badge.svg?branch=master)](https://coveralls.io/github/bingoohuang/voucher-no?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.bingoohuang/voucher-no/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.github.bingoohuang/voucher-no/)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

# voucher-no
Random Voucher No generator based on bloom filter and redis bit set.

```java
// create voucher no generator to generate fixed length of 12 base on redis bitset with key Voucher:No
VoucherNo voucherNo = new VoucherNo(new Jedis("127.0.0.1", 6379), "Voucher:No", 12);

String no = voucherNo.next();

// 438744088222


```


