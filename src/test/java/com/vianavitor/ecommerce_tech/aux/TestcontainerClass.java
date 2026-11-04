package com.vianavitor.ecommerce_tech.aux;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@Testcontainers
public class TestcontainerClass {
    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
            .withUsername("root")
            .withPassword("*123*")
            .withDatabaseName("ecommerce_tech");

}
