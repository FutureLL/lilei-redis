package com.future.redisLock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;

/**
 * @author Mr.Li
 * @version 1.0
 * @Description: Redis分布式锁
 * @Modified By:
 * @date 2018/11/29 23:05
 */
public class RedisLock {

    /**
     * 获取锁
     *
     * @param key
     * @param timeout
     * @return
     */
    public String getLock(String key, int timeout) {
        try {
            Jedis jedis = RedisManager.getJedis();
            /**
             * UUID含义是通用唯一识别码 (Universally Unique Identifier)，
             *    这是一个软件建构的标准，也是被开源软件基金会 (Open Software Foundation, OSF)
             *    的组织在分布式计算环境 (Distributed Computing Environment, DCE) 领域的一部份。
             *
             * UUID 的目的，是让分布式系统中的所有元素，都能有唯一的辨识资讯，
             *    而不需要透过中央控制端来做辨识资讯的指定
             */

            String value = UUID.randomUUID().toString();
            long end = System.currentTimeMillis() + timeout;
            //阻塞
            while (System.currentTimeMillis() < end) {
                if (jedis.setnx(key, value) == 1) {
                    jedis.expire(key, timeout);
                    //表示锁设置成功，Redis操作成功
                    return value;
                }
                //检测过期时间
                //-1:表示这个key没有设定过期时间
                if (jedis.ttl(key) == -1) {
                    jedis.expire(key, timeout);
                }
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 释放锁
     *
     * @param key
     * @param value
     * @return
     */
    public boolean releastLock(String key, String value) {

        try {
            Jedis jedis = RedisManager.getJedis();
            //变成一个阻塞的操作
            while (true) {
                //watch:用来监控一个或多个key
                /**
                 * 一旦key被修改或者删除，后边if中的代码就不会执行，这个监控直到if中的事务执行完才结束
                 * 防止:其他地方调用这里，对这里进行修改
                 */
                jedis.watch(key);
                //判断获得锁的线程和当前redis中存的锁是同一个
                if (value.equals(jedis.get(key))) {
                    Transaction transaction = jedis.multi();
                    transaction.del(key);
                    List<Object> list = transaction.exec();
                    if (list == null) {
                        continue;
                    }
                }
                jedis.unwatch();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        RedisLock redisLock = new RedisLock();
        String lockId = redisLock.getLock("lock:aaa", 10000);
        if (lockId != null) {
            //这里这能调用该一次，之后不管执行几次都会返回失败，不会执行这个if
            System.out.println("获得锁成功");
        }

        System.out.println("失败");

        //再次调用获得锁的方法
        String lockId1 = redisLock.getLock("lock:aaa", 10000);
        //知道getLock方法中的堵塞，直到超时了还没有获得锁，那么返回null
        System.out.println(lockId1);
    }
}
