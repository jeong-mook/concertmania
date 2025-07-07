package com.concertmania;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ConcertManiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConcertManiaApplication.class, args);
    }

}
