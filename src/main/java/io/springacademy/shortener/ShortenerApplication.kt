package io.springacademy.shortener

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@SpringBootApplication
class ShortenerApplication {
    @Bean
    fun indexRouter(@Value("classpath:/static/index.html") indexHtml: Resource): RouterFunction<ServerResponse> {
        return router {
            GET("/") {
                ok().contentType(MediaType.TEXT_HTML)
                    .syncBody(indexHtml)
            }
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ShortenerApplication::class.java, *args)
}

