package io.springacademy.shortener

import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository

import reactor.core.publisher.Mono

@Repository
class RedisLinkRepository(private val redis: ReactiveRedisOperations<String, String>) : LinkRepository {

    override fun save(link: Link): Mono<Link> {
        return redis.opsForValue()
            .set(link.key, link.originalLink)
            .map { link }
    }

    override fun findById(key: String): Mono<Link> {
        return redis.opsForValue()
            .get(key)
            .map { Link(key, it) }
    }
}
