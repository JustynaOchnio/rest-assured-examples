package com.testinglaboratory.restassured.primer;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static io.restassured.RestAssured.when;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalToCompressingWhiteSpace;

class PrimerInformationTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:8082";
        RestAssured.basePath = "/challenge/primer";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build();
    }

    @Test
    void checkInformation() {
        when().get("/information")
                .then().log().ifValidationFails()
                .statusCode(SC_OK)
                .body("message", equalToCompressingWhiteSpace("Oi! W'at can I do for ya?" +
                        " In this primer for challenges you'll learn how to look for flags." +
                        " Remember that this is not purely technical task." +
                        " You'll role play and use your knowledge to find treasures your looking for." +
                        " If you have any questions - ask." +
                        " Try and found as many flags as possible.(Five, there are five.)" +
                        " begin with shooting at /tryout. "));
    }

    @Test
    void checkTryout() {
        when().get("/tryout")
                .then().log().ifValidationFails()
                .statusCode(SC_OK)
                .body("message", equalToCompressingWhiteSpace(
                        "Good! Toy have tried to GET a resource." +
                                "Now you have to GET something else... /flag"
                ));
    }

    @Test
    void checkFlagInformation() {
        Response response = when().get("/flag")
                .then().log().ifValidationFails()
                .statusCode(SC_OK)
                .extract()
                .response();

        assertThat(response.jsonPath().getString("flag"))
                .isEqualTo("A flag has a form of ${<flag_name>}");
        assertThat(response.jsonPath().getString("message"))
                .isEqualTo("Use your exploratory skills and feel the challenge's theme to obtain flags");
    }
}
