package io.springacademy.shortener;

import org.junit.Test;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LinkServiceTest {

    private LinkRepository linkRepository = mock(LinkRepository.class);
    private LinkService linkService = new LinkService("http://localhost:8080", linkRepository);

    @Test
    public void shouldReturnShortenedLinkForConfiguredDomain() {
        when(linkRepository.save(any())).thenAnswer(answer -> Mono.just(answer.getArguments()[0]));

        StepVerifier.create(linkService.shortenLink("http://some-link.com"))
                    .expectNextMatches(result -> result.startsWith("http://localhost:8080/")
                                                 && result.length() > "http://localhost:8080/".length())
                    .expectComplete()
                    .verify();
    }

    @Test
    public void shouldReturnOriginalLinkForShortenedLink() {
        when(linkRepository.save(any()))
                .thenAnswer(answer -> Mono.just(answer.getArguments()[0]));
        when(linkRepository.findById(anyString()))
                .thenAnswer(answer -> Mono.just(new Link((String) answer.getArguments()[0],
                                                         "http://some-link.com")));
        StepVerifier.create(linkService.shortenLink("http://some-link.com")
                                       .flatMap(shortenedLink -> linkService.findOriginalLink(shortenedLink)))
                    .expectNext("http://some-link.com")
                    .expectComplete()
                    .verify();

    }

}
