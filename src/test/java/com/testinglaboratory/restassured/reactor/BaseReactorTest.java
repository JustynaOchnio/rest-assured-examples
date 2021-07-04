package com.testinglaboratory.restassured.reactor;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseReactorTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:8083";
        RestAssured.basePath = "/challenge/reactor";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build();

    }


//    private String key = getKeyFromDesk();
//
//    private String getKeyFromDesk(){
//    }

}
