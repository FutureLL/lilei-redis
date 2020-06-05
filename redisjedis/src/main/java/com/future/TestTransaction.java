package com.future;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * @description: Redis 事务
 * @author: Mr.Li
 * @date: Created in 2020/5/31 19:34
 * @version: 1.0
 * @modified By:
 */
public class TestTransaction {

    public static void main(String[] args) {
        // new Jedis 对象
        Jedis jedis = new Jedis("127.0.0.1", 6379);

        // 清空数据
        jedis.flushDB();

        // 开启事务
        Transaction multi = jedis.multi();

        try {
            // 事务操作
            multi.set("user1", "future");

            // 代码抛出一个异常事务,执行失败
            // int i = 1 / 0;

            // 执行事务
            multi.exec();
        } catch (Exception e) {
            // 放弃事务
            multi.discard();
            e.printStackTrace();
        } finally {
            System.out.println(jedis.get("user1"));
            // 关闭连接
            jedis.close();
        }
    }
}
