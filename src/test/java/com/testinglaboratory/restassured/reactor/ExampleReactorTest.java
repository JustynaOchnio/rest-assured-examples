package com.testinglaboratory.restassured.reactor;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

public class ExampleReactorTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:8083";
        RestAssured.basePath = "/challenge/reactor";
    }

    @Test
    void checkInformation(){
       when().get("/information")
       .then().log().everything();
    }
}
