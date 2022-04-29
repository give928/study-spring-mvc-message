package com.give928.spring.mvc.itemservice.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
class MessageSourceTest {
    @Autowired
    MessageSource ms;

    @Test
    void helloMessage() {
        String hello = ms.getMessage("hello", null, null);
        assertEquals("안녕", hello);
    }

    @Test
    void notFoundMessageCode() {
        assertThrows(NoSuchMessageException.class, () -> ms.getMessage("no_code", null, null));
    }

    @Test
    void notFoundMessageCodeDefaultMessage() {
        String result = ms.getMessage("no_code", null, "기본 메시지", null);
        assertEquals("기본 메시지", result);
    }

    @Test
    void argumentMessage() {
        String result = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
        assertEquals("안녕 Spring", result);
    }

    @Test
    void defaultLang() {
        assertEquals("안녕", ms.getMessage("hello", null, null));
        assertEquals("안녕", ms.getMessage("hello", null, Locale.KOREA));
    }

    @Test
    void enLang() {
        assertEquals("hello", ms.getMessage("hello", null, Locale.ENGLISH));
    }
}
