package com.zslin.cache;

import com.zslin.basic.tools.ConfigTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 8:53.
 */
@Component
public class CacheTools {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ConfigTools configTools;

    /**
     * 重新构造Key，防止多项目中key重复
     * @param key
     * @return
     */
    private String rebuildKey(String key) {
        return configTools.getName()+"-"+key;
    }

    /**
     * 添加缓存
     * @param key 键
     * @param value 值
     * @param timeout 失效时间，单位秒，为空或负数则长期有效
     */
    public void putKey(String key, Object value, Integer timeout) {
        key = rebuildKey(key);
        redisTemplate.opsForValue().set(key, value);
        if(timeout != null && timeout>0) {
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    public Object setAndGet(String key, Object value, Integer timeout) {
        key = rebuildKey(key);
        Object res = redisTemplate.opsForValue().getAndSet(key, value);
        if(timeout != null && timeout>0) {
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
        return res;
    }

    public Object getKey(String key) {
        key = rebuildKey(key);
        return redisTemplate.opsForValue().get(key);
    }

    public void insc(String key, Double amount) {
        key = rebuildKey(key);
        redisTemplate.opsForValue().increment(key, amount);
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        String myKey = rebuildKey(key);
        if (redisTemplate != null) {
            return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection connection)
                        throws DataAccessException {
                    RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                    byte[] keys = serializer.serialize(myKey);
                    return connection.exists(keys);
                }
            });
        }
        return false;
    }
}
