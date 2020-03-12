package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.FlywayTestExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * DemoApplicationIT
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith({
    SpringExtension.class, 
    FlywayTestExtension.class
})
@ActiveProfiles("test")
@DisplayName("Integration Test of the Todo Controller")
public class DemoApplicationIT {

    final static String baseUrl = "http://localhost";

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;
    
    @Test
	public void contexLoads() throws Exception {
		assertNotNull(restTemplate);
    }
     
    @Test
    @FlywayTest(locationsForMigrate = {"/db/migration/todos"})
    public void getTodos_ReturnsTodos_OK() throws Exception {
    }
    
}