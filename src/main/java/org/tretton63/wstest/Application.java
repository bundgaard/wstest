package org.tretton63.wstest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.tretton63.wstest.service.FileUploader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private final FileUploader uploader;
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    Application(FileUploader uploader, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.uploader = uploader;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }

    String readLine(FileChannel inChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(2048); // 2KB per line
        try {
            long lastPos = inChannel.position();
            int length = 0;
            if ((length = inChannel.read(buffer)) > 0) {
                boolean foundTerminator = false;
                boolean foundLineTerminator = false;
                byte[] data = Arrays.copyOfRange(buffer.array(), 0, length);
                int endPosition = 0;
                for (byte b : data) {
                    endPosition++;
                    switch (b) {
                        case -1:
                            foundTerminator = true;
                            break;
                        case (byte) '\r':
                        case (byte) '\n':
                            foundLineTerminator = true;
                            break;
                        default:
                            if (foundLineTerminator) {
                                endPosition--;
                                foundTerminator = true;
                            }
                    }
                    if (foundTerminator) {
                        break;
                    }

                }
                inChannel.position(lastPos + endPosition);
                if (foundTerminator) {
                    return new String(data, 0, endPosition, StandardCharsets.UTF_8);
                }
                logger.info("line: {} {}", lastPos, new String(data, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(@Value("${wstest.bucket}") String bucket) {

        return args -> {
            uploader.listBucket(bucket).forEach(file -> logger.info("{} ", file));
            inMemoryUserDetailsManager.createUser(User.withUsername("test").password("{noop}foo").roles("USER").build());

            /*ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
            executorService.scheduleWithFixedDelay(() -> {
                try (FileChannel fileChannel = FileChannel.open(Paths.get("c:\\code\\test.txt"), StandardOpenOption.READ)) {
                    String line = readLine(fileChannel);
                    logger.info("reading {}", line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, 3L, 5L, SECONDS);
            executorService.scheduleWithFixedDelay(() -> ChatMessageHandler.broadcastLogEntry(
                    new Message(
                            "Hello",
                            OffsetDateTime.now(ZoneId.of("Europe/Stockholm")
                            )
                    )
            ), 3L, 5L, SECONDS);*/
        };


    }

}
