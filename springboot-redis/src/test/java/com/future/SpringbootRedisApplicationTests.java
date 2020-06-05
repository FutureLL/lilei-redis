package com.future;

import com.future.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class SpringbootRedisApplicationTests {

    @Autowired
    // 我们自己定义的 redisTemplate
    @Qualifier("redisTemplate")
    // redisTemplate: 操作不同数据类型
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtils redisUtil;

    @Test
    void contextLoads() {

        // opsForValue(): 操作字符串,类似 String 类型
        redisTemplate.opsForValue();
        // opsForList(): 操作链表,类似 List 类型
        redisTemplate.opsForList();
        // opsForHash(): 操作哈希,类似 Hash 类型
        redisTemplate.opsForHash();
        // opsForSet(): 操作集合,类似 set 类型
        redisTemplate.opsForSet();
        // opsForZSet(): 操作有序集合,类似 zset 类型
        redisTemplate.opsForZSet();

        // 除了基本的操作,我们常用的方法都可以直接通过 redisTemplate 操作,比如: 事务和基本的增删改查(CRUD)

        // 获取 Redis 的连接对象
        // RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        // connection.flushDb();
        // connection.flushAll();
        // connection.ping();

        redisTemplate.opsForValue().set("mykey", "FutureLL");
        System.out.println(redisTemplate.opsForValue().get("mykey"));
    }

    @Test
    void testRedisTemplate() {

        redisTemplate.opsForValue().set("user", "FutureLL");
        System.out.println(redisTemplate.opsForValue().get("user"));
    }

    @Test
    void testRedisUtil() {
        redisUtil.set("userLWY","FutureLWY");
        System.err.println(redisUtil.get("userLWY"));
    }
}
