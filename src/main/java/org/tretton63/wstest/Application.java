package org.tretton63.wstest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.tretton63.wstest.service.CalculatorService;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Application {


    private final CalculatorService calculatorService;
    private final CalculatorService calculatorService1;


    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public Application(CalculatorService calculatorService, CalculatorService calculatorService1) {
        this.calculatorService = calculatorService;
        this.calculatorService1 = calculatorService1;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            logger.info("calc is the same as calc1 == {}", calculatorService == calculatorService1);
            logger.info("address of calc {}", calculatorService);
            logger.info("address of calc1 {}", calculatorService1);
            logger.info("calc 1 + 1 = {}", calculatorService.add(1, 1));
            logger.info("calc1 1 + 1 = {}", calculatorService1.add(1, 1));

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
            executorService.scheduleWithFixedDelay(() -> ChatMessageHandler.broadcastLogEntry(
                    new Message(
                            "Hello",
                            OffsetDateTime.now(ZoneId.of("Europe/Stockholm")
                            )
                    )
            ), 3L, 5L, TimeUnit.SECONDS);

        };
    }

}
