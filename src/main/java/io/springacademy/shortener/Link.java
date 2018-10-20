package io.springacademy.shortener;

import lombok.Value;

@Value
public class Link {

    private String key;
    private String originalLink;
}
