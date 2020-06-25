# JavaCacheOptions


## Requirements
- thread safety
- get, put, delete option
- expiration option is a plus
- auto refresh option is a plus
- file backed persistent option is a plus
- distributed cache is not required


## Test scenario 
- 1 million records insert and fetch 
- key and value are serialized as bytes
- key is int 
- value is a string of 1000 random bytes


## Measure
- write eps
- read eps
- heap utilization
 
 
 
 |Solution              | Read EPS | Write EPS|
 |----------------------|------------------|-----------------|
 |ConcurrentMap         | | |
 |Guava Cache           | | | |
 |Hazelcast Local Cache | | | |
 |Hazelcast Dist Cache  | | | |
 |MapDB Cache           | | | |
 |RocksDB Cache         | | | |
  
    