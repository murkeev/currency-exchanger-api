package murkeev.currencyexchangerapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public String createCacheKey(String entityType, Object value) {
        String rawKey = entityType + ":" + value.toString();
        return DigestUtils.md5DigestAsHex(rawKey.getBytes(StandardCharsets.UTF_8));

    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void saveValue(String key, Object response, long duration, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, response, duration, unit);
    }

    public void removeValue(String key) {
        redisTemplate.delete(key);
    }
}
