package io.springacademy.shortener;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.Value;
import reactor.core.publisher.Mono;

@RestController
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/link")
    Mono<ResponseEntity<Object>> create(@RequestBody CreateLinkRequest request) {
        try {
            new URL(request.getLink());
        } catch (MalformedURLException e) {
            return Mono.just(ResponseEntity.badRequest()
                                           .body(new Message(request.getLink() + " is not a valid URL")));
        }
        return linkService.shortenLink(request.getLink())
                          .map(link -> ResponseEntity.ok(new CreateLinkResponse(link)));
    }

    @GetMapping("/{key}")
    Mono<ResponseEntity<Object>> getLink(@PathVariable String key) {
        return linkService.findOriginalLink(key)
                          .map(link -> ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                                                     .header("Location", link)
                                                     .build())
                          .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Value
    static class Message {

        String message;
    }

    @Value
    public static class CreateLinkRequest {

        String link;

    }

    @Value
    static
    class CreateLinkResponse {

        String shortenedLink;

    }
}

