package io.springacademy.shortener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisLinkRepositoryTest {

    @Autowired
    private RedisLinkRepository repository;

    @Test
    public void shouldSaveLink() {
        Link link = new Link("some-key", "http://spring.io");
        StepVerifier.create(repository.save(link))
                    .expectNext(link)
                    .verifyComplete();
    }

    @Test
    public void shouldFindLinkById() {
        Link link = new Link("some-key", "http://spring.io");
        StepVerifier.create(repository.save(link)
                                      .flatMap(result -> repository.findById(result.getKey())))
                    .expectNext(link)
                    .verifyComplete();
    }

}
