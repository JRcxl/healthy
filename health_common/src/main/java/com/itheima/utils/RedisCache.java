package com.itheima.utils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.math.BigInteger;
import java.util.Set;
public class RedisCache {
    private static final String REDIS_KEY_PREFIX = "qa_hash:";

    // 存储问题与答案
    public static void cacheQuestion(String question, String answer, BigInteger simHash) {
        try (Jedis jedis = new Jedis("localhost")) {
            String key = REDIS_KEY_PREFIX + simHash;
            jedis.hset(key, "answer", answer);
            jedis.hset(key, "question", question);
            jedis.expire(key, 86400); // TTL 24小时
        }
    }

    // 查询相似问题
    public static String findSimilarAnswer(BigInteger simHash, int hammingDistance) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        try (JedisPool jedisPool = new JedisPool(poolConfig, "localhost")) {
            try (Jedis jedis = jedisPool.getResource()) { // 使用连接池
                Set<String> keys = jedis.keys(REDIS_KEY_PREFIX + "*");
                for (String key : keys) {
                    String storedHashStr = key.split(":")[1];
                    BigInteger storedHash = new BigInteger(storedHashStr);
                    if (hammingDistance(storedHash, simHash) <= hammingDistance) {
                        // 每次查询时刷新过期时间（可选）
                        jedis.expire(key, 86400); // 重置 TTL
                        return jedis.hget(key, "answer");
                    }
                }
                return null;
            }
        }
    }

    // 计算汉明距离
    private static int hammingDistance(BigInteger hash1, BigInteger hash2) {
        BigInteger xor = hash1.xor(hash2);
        return xor.bitCount(); // 直接计算二进制位差异
    }

}
