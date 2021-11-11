package org.tretton63.wstest.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class MessageBus {

    @Bean
    public BlockingQueue<Message> blockingQueue() {
        return new LinkedBlockingQueue<>();
    }
}
