package io.springacademy.shortener;

import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public class RedisLinkRepository implements LinkRepository {

    private final ReactiveRedisOperations<String, String> redis;

    public RedisLinkRepository(ReactiveRedisOperations<String, String> redis) {
        this.redis = redis;
    }

    @Override
    public Mono<Link> save(Link link) {
        return redis.opsForValue()
                    .set(link.getKey(), link.getOriginalLink())
                    .map(result -> link);
    }

    @Override
    public Mono<Link> findById(String key) {
        return redis.opsForValue()
                    .get(key)
                    .map(result -> new Link(key, result));
    }
}
