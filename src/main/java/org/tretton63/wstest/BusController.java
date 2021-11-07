package org.tretton63.wstest;

import org.springframework.cglib.core.Block;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.BatchUpdateException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.BlockingQueue;

@RestController
public class BusController {

    private final BlockingQueue<Message> messageBus;

    public BusController(BlockingQueue<Message> messageBus) {
        this.messageBus = messageBus;
    }

    @PostMapping("/produce")
    public void produce() {
        messageBus.add(new Message("LOREM IPSUM DOLOR", OffsetDateTime.now(ZoneId.of("Europe/Stockholm"))));
    }


    @GetMapping("/consume")
    public Message consume() throws InterruptedException {
        return messageBus.take();
    }
}
