package com.restaurantapp.ndnhuy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;

@Slf4j
@Configuration
public class TestcontainersConfiguration {

//    @Container
//    @ServiceConnection
//    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.31");

    @Bean
    @ConditionalOnProperty(name = "app.testcontainers.enabled", havingValue = "true")
    @ServiceConnection
    public MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>("mysql:8.0.31").withReuse(true);
    }

}
