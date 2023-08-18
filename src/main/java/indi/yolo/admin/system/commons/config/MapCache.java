package indi.yolo.admin.system.commons.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author yoloz
 */
@Slf4j
@Configuration
public class MapCache {

    private final Map<String, CacheValue> CACHE_MAP = new ConcurrentHashMap<>();

    private static class CacheValue {
        long expireTime;
        Object object;

        CacheValue(Long expireTime, Object object) {
            this.expireTime = expireTime;
            this.object = object;
        }
    }

    public void put(String key, Object obj) {
        put(key, obj, 0, null);
    }

    public void put(String key, Object obj, int time, TimeUnit timeUnit) {
//        triggerExpireTime();
        long expireTime = 0L;
        if (time > 0) {
            expireTime = TimeUnit.MILLISECONDS.convert(time, timeUnit) + System.currentTimeMillis();
        }
        CacheValue cacheValue = CACHE_MAP.get(key);
        if (cacheValue == null) {
            cacheValue = new CacheValue(expireTime, obj);
        } else {
            cacheValue.expireTime = expireTime;
            cacheValue.object = obj;
        }
        CACHE_MAP.put(key, cacheValue);
    }

    public Optional<Object> get(String key) {
        if (key == null) return Optional.empty();
        triggerExpireTime();
        CacheValue value = CACHE_MAP.get(key);
        if (value == null) return Optional.empty();
        return Optional.of(value.object);
    }

    public void clear() {
        CACHE_MAP.clear();
    }

    public void remove(String key) {
        CACHE_MAP.remove(key);
    }

    //添加触发整个缓存清理过期数据
    private void triggerExpireTime() {
        long current = System.currentTimeMillis();
        for (Map.Entry<String, CacheValue> entry : CACHE_MAP.entrySet()) {
            long expireTime = entry.getValue().expireTime;
            if (expireTime == 0L) continue;
            if (expireTime < current) {
                log.debug("map cache expire:" + entry.getKey());
                remove(entry.getKey());
            }
        }
    }
}
