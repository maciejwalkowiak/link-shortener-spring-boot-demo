package io.springacademy.shortener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = LinkController.class)
public class ApiTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private LinkService linkService;

    @Test
    public void shouldReturnShortenedLink() {
        when(linkService.shortenLink("http://any-link.com")).thenReturn(Mono.just("http://shortened-link"));

        client.post()
              .uri("/link")
              .contentType(MediaType.APPLICATION_JSON)
              .syncBody("{\"link\":\"http://any-link.com\"}")
              .exchange()
              .expectStatus()
              .is2xxSuccessful()
              .expectBody()
              .jsonPath("$.shortenedLink")
              .value(val -> assertThat(val).isEqualTo("http://shortened-link"));
    }

    @Test
    public void shouldReturnInvalidContentTypeForInvalidUrl() {
        client.post()
              .uri("/link")
              .contentType(MediaType.APPLICATION_JSON)
              .syncBody("{\"link\":\"invalid link\"}")
              .exchange()
              .expectStatus()
              .isBadRequest();
    }

    @Test
    public void shouldRedirectToActualLink() {
        when(linkService.findOriginalLink("shortened-link")).thenReturn(Mono.just("http://some-link.com"));

        client.get()
              .uri("/shortened-link")
              .exchange()
              .expectStatus()
              .isPermanentRedirect()
              .expectHeader().value("Location", location -> assertThat(location).isEqualTo("http://some-link.com"));
    }
}
