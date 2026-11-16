package com.vianavitor.ecommerce_tech.aux;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
public class TestcontainerClass {
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
            .withUsername("root")
            .withPassword("*123*")
            .withDatabaseName("ecommerce_tech");

    static {
        // Share one container across test classes; Testcontainers cleans it up when the JVM exits.
        mysql.start();
    }

}
