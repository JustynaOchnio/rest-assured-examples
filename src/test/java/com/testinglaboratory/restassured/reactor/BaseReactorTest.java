package com.testinglaboratory.restassured.reactor;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeAll;

import java.util.Locale;
import java.util.Map;

public abstract class BaseReactorTest implements WithAssertions {
    private final static Faker faker = new Faker(Locale.UK);
    protected static String randomizedUserName;

    protected String randomizeUser(){
        return faker.name().username();
    }

    protected String userKey = getUserKey();
    protected String getUserKey() {
        return RestAssured
                .given()
                .body(Map.of("name", BaseReactorTest.randomizedUserName))
                .when().post("/desk")
                .thenReturn().getBody().jsonPath().getString("key");
    }

    @BeforeAll
    public static void setUp() {
        randomizedUserName = faker.name().username();
        RestAssured.baseURI = "http://localhost:8083";
        RestAssured.basePath = "/challenge/reactor";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build();
    }
}
