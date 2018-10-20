package io.springacademy.shortener;

import reactor.core.publisher.Mono;

public interface LinkRepository {

    Mono<Link> save(Link link);

    Mono<Link> findById(String key);
}
