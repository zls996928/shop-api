package com.fh.shop.api.utils;

import redis.clients.jedis.Jedis;

public class    RedisUtil {

    public static  void set(String key,String value){
        Jedis resource = null;
        try {
            resource=RedisPool.getResource();
            resource.set(key,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if(resource!=null){
                resource.close();
            }
        }

    }

    public static  String get (String key){
        Jedis resource=null;
        String result=null;
        try {
            resource = RedisPool.getResource();
             result = resource.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {

            if (resource!=null){
                resource.close();
            }
        }
        return result;
    }

    public static  boolean exists  (String key){
        Jedis resource=null;
        boolean exist;
        try {
            resource = RedisPool.getResource();
            exist = resource.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {

            if (resource!=null){
                resource.close();
            }
        }
        return exist;
    }

    public static  Long  del(String key){
        Jedis resource=null;
        try {
            resource = RedisPool.getResource();
           return resource.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (resource!=null){
                resource.close();
            }
        }
    }

    public static  void  delBatch(String... keys){
        Jedis resource=null;
        try {
            resource = RedisPool.getResource();
            resource.del(keys);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (resource!=null){
                resource.close();
            }
        }
    }

    public static  void  setEx(String key,int seconds,String value){
        Jedis resource=null;
        try {
            resource = RedisPool.getResource();
            resource.setex(key,seconds,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (resource!=null){
                resource.close();
            }
        }
    }


    public static  void expire(String key,int seconds){
        Jedis resource=null;
        try {
            resource = RedisPool.getResource();
            resource.expire(key,seconds);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (resource!=null){
                resource.close();
            }
        }
    }
}
