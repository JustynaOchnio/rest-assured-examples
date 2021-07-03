package com.testinglaboratory.restassured.reactor;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

//TODO EXERCISE create tests for Reactor challenge
public class ExerciseReactorTest {
    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:8083";
        RestAssured.basePath = "/challenge/reactor";
    }

    @Test
    void checkInTest(){

    }
}
