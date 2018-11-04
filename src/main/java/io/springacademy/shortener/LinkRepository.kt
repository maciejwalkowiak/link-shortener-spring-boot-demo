package io.springacademy.shortener

import reactor.core.publisher.Mono

interface LinkRepository {

    fun save(link: Link): Mono<Link>

    fun findById(key: String): Mono<Link>
}
