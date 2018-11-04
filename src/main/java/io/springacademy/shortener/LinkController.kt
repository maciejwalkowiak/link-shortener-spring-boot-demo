package io.springacademy.shortener

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.net.MalformedURLException
import java.net.URL

@RestController
class LinkController(private val linkService: LinkService) {

    @PostMapping("/link")
    fun create(@RequestBody request: CreateLinkRequest): Mono<ResponseEntity<Any>> {
        return if (request.link.isUrl()) {
            linkService.shortenLink(request.link)
                .map { ResponseEntity.ok<Any>(CreateLinkResponse(it)) }
        } else {
            ResponseEntity.badRequest()
                .body<Any>(Message(request.link + " is not a valid URL"))
                .toMono()
        }
    }

    @GetMapping("/{key}")
    fun getLink(@PathVariable key: String): Mono<ResponseEntity<Any>> {
        return linkService.findOriginalLink(key)
            .map {
                ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                    .header("Location", it)
                    .build<Any>()
            }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }
}

data class Message(val message: String)
data class CreateLinkRequest(val link: String)
data class CreateLinkResponse(val shortenedLink: String)

fun String.isUrl(): Boolean {
    return try {
        URL(this)
        true
    } catch (e: MalformedURLException) {
        false
    }
}
