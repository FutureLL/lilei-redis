package com.future;

import redis.clients.jedis.Jedis;

/**
 * @description: 测试连接
 * @author: Mr.Li
 * @date: Created in 2020/5/31 19:05
 * @version: 1.0
 * @modified By:
 */
public class TestPing {

    public static void main(String[] args) {
        // 1、new Jedis 对象
        Jedis jedis = new Jedis("127.0.0.1", 6379);

        // jedis 所有的命令就是之前在命令行操作的命令
        // ping 命令连接成功返回 PONG
        System.out.println(jedis.ping());

    }
}
