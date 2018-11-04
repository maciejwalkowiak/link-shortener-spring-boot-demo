package io.springacademy.shortener

import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.toMono

@RunWith(SpringRunner::class)
@WebFluxTest(controllers = [LinkController::class])
class ApiTest {

    @Autowired
    private lateinit var client: WebTestClient

    @MockBean
    private lateinit var linkService: LinkService

    @Test
    fun shouldReturnShortenedLink() {
        whenever(linkService.shortenLink("http://any-link.com")).thenReturn("http://shortened-link".toMono())

        client.post()
            .uri("/link")
            .contentType(MediaType.APPLICATION_JSON)
            .syncBody("{\"link\":\"http://any-link.com\"}")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody()
            .jsonPath("$.shortenedLink")
            .value<String> { assertThat(it).isEqualTo("http://shortened-link") }
    }

    @Test
    fun shouldReturnInvalidContentTypeForInvalidUrl() {
        client.post()
            .uri("/link")
            .contentType(MediaType.APPLICATION_JSON)
            .syncBody("{\"link\":\"invalid link\"}")
            .exchange()
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun shouldRedirectToActualLink() {
        whenever(linkService.findOriginalLink("shortened-link")).thenReturn("http://some-link.com".toMono())

        client.get()
            .uri("/shortened-link")
            .exchange()
            .expectStatus()
            .isPermanentRedirect
            .expectHeader()
            .value("Location") { location -> assertThat(location).isEqualTo("http://some-link.com") }
    }
}
