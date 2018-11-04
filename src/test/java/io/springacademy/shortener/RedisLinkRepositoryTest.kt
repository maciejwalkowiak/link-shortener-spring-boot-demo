package io.springacademy.shortener

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import reactor.test.test

@RunWith(SpringRunner::class)
@SpringBootTest
class RedisLinkRepositoryTest {

    @Autowired
    private lateinit var repository: RedisLinkRepository

    @Test
    fun shouldSaveLink() {
        val link = Link("some-key", "http://spring.io")
        repository.save(link)
            .test()
            .expectNext(link)
            .verifyComplete()
    }

    @Test
    fun shouldFindLinkById() {
        val link = Link("some-key", "http://spring.io")
        repository.save(link)
            .flatMap { (key) -> repository.findById(key) }
            .test()
            .expectNext(link)
            .verifyComplete()
    }

}
