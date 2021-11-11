package org.tretton63.wstest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.tretton63.wstest.infrastructure.ChatMessageHandler;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@EnableWebSocket
@EnableWebSecurity

public class Config extends WebSecurityConfigurerAdapter implements WebSocketConfigurer {

    @Value("${aws.secretAccessKey}")
    private String awsSecretKey;
    @Value("${aws.accessKeyId}")
    private String awsAccessKeyId;
    @Value("${wstest.blob-url}")
    private String blobUrl;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatMessageHandler(), "/chat");
    }

    /*@Bean public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
*/


    @Bean
    protected InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        logger.info("Startin in memory details manager");
        return new InMemoryUserDetailsManager();
    }


    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Bean
    public JsonMapper jsonMapper() {
        return JsonMapper.builder()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .addModule(new JavaTimeModule())
                .build();
    }

    static class SpringCredentials implements AwsCredentials {

        private final String accessKey;
        private final String secretKey;

        public SpringCredentials(String accessKey, String secretKey) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }


        @Override
        public String accessKeyId() {
            return accessKey;
        }

        @Override
        public String secretAccessKey() {
            return secretKey;
        }
    }

    @Bean
    public S3Client s3Client() {

        return S3Client.builder()
                .region(Region.EU_CENTRAL_1)
                .endpointOverride(URI.create(blobUrl))
                .credentialsProvider(() -> new SpringCredentials(awsAccessKeyId, awsSecretKey))
                .build();
    }

    /*
    * @Bean
    public EmbeddedEventStore embeddedEventStore(EventStorageEngine eventStorageEngine, AxonConfiguration axonConfiguration) {
        return EmbeddedEventStore.builder()
                .messageMonitor(
                        axonConfiguration.messageMonitor(
                                EventStore.class,
                                "eventStore"
                        )
                )
                .storageEngine(eventStorageEngine)
                .build();
    }

    @Bean
    public EventStorageEngine eventStorageEngine(Serializer defaultSerializer,
                                                 PersistenceExceptionResolver persistenceExceptionResolver,
                                                 @Qualifier("eventSerializer") Serializer eventSerializer,
                                                 AxonConfiguration axonConfiguration,
                                                 EntityManagerProvider provider,
                                                 TransactionManager transactionManager,
                                                 ConnectionProvider connectionProvider) {
        return JdbcEventStorageEngine.builder()
                .connectionProvider(connectionProvider)
                .eventSerializer(eventSerializer)
                .snapshotSerializer(defaultSerializer)
                .upcasterChain(axonConfiguration.upcasterChain())
                .persistenceExceptionResolver(persistenceExceptionResolver)
                .transactionManager(transactionManager)
                .build();

    }
    * */

    private static final Logger logger = LoggerFactory.getLogger(Config.class);


}
