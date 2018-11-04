package io.springacademy.shortener

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import reactor.core.publisher.toMono
import reactor.test.test

class LinkServiceTest {

    private val linkRepository : LinkRepository = mock()
    private val linkService = LinkService("http://localhost:8080", linkRepository)

    @Test
    fun shouldReturnShortenedLinkForConfiguredDomain() {
        whenever(linkRepository.save(any())).thenAnswer { answer -> answer.arguments[0].toMono() }

        linkService.shortenLink("http://some-link.com")
            .test()
            .expectNextMatches { it.startsWith("http://localhost:8080/") && it.length > "http://localhost:8080/".length }
            .expectComplete()
            .verify()
    }

    @Test
    fun shouldReturnOriginalLinkForShortenedLink() {
        whenever(linkRepository.save(any()))
            .thenAnswer { it.arguments[0].toMono() }
        whenever(linkRepository.findById(anyString()))
            .thenAnswer {
                Link(it.arguments[0] as String, "http://some-link.com").toMono()
            }
        linkService.shortenLink("http://some-link.com")
            .flatMap { linkService.findOriginalLink(it) }
            .test()
            .expectNext("http://some-link.com")
            .expectComplete()
            .verify()

    }

}
