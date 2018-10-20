package io.springacademy.shortener;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class LinkService {

    private final String domain;
    private final LinkRepository linkRepository;

    public LinkService(@Value("${domain}") String domain, LinkRepository linkRepository) {
        this.domain = domain;
        this.linkRepository = linkRepository;
    }

    Mono<String> shortenLink(String link) {
        String randomKey = RandomStringUtils.randomAlphabetic(8);
        return linkRepository.save(new Link(randomKey, link))
                             .map(savedLink -> domain + "/" + randomKey);
    }

    Mono<String> findOriginalLink(String key) {
        return linkRepository.findById(key)
                             .map(Link::getOriginalLink);
    }
}
