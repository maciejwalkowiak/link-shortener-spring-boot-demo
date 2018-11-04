package io.springacademy.shortener

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import reactor.core.publisher.Mono

@Service
class LinkService(
    @Value("\${domain}") private val domain: String,
    private val linkRepository: LinkRepository
) {

    fun shortenLink(link: String): Mono<String> {
        val randomKey = RandomStringUtils.randomAlphabetic(8)
        return linkRepository.save(Link(randomKey, link))
            .map { "$domain/$randomKey" }
    }

    fun findOriginalLink(key: String): Mono<String> {
        return linkRepository.findById(key)
            .map { it.originalLink }
    }
}
