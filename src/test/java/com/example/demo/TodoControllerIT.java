package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.FlywayTestExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;


import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith({
    SpringExtension.class, 
    FlywayTestExtension.class
})
@ActiveProfiles("test")
@DisplayName("Integration Test of the Todo Controller")
public class TodoControllerIT {

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    public void initialiseRestAssuredMockMvcWebApplicationContext() {
      RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }
    
    @Test
	  public void contexLoads() throws Exception {
      assertNotNull(webApplicationContext);
    }

    @Test
    @FlywayTest(locationsForMigrate = {"/db/test/todos"})
    public void getTodos_ReturnsTodos_OK() throws Exception {
      given()
        .when()
          .get("/api/v1/todos")
        .then()
          .log().ifValidationFails()
          .statusCode(200)
          .contentType(ContentType.JSON)
          .body("size()", is(3));
    }
    
}