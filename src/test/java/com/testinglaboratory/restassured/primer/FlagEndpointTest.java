package com.testinglaboratory.restassured.primer;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FlagEndpointTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:8082";
        RestAssured.basePath = "/challenge/primer";
    }

    @Test
    void getHelloFlag() {
        Response response = when().get("/flag/1")
                .then().log().ifValidationFails()
                .statusCode(SC_OK)
                .extract()
                .response();

        assertThat(response.jsonPath().getString("flag"))
                .isEqualTo("${flag_hello_there}");
    }

    @Test
    void getHelloFlagStatus() {
        when().get("/flag/1")
                .then().log().ifValidationFails()
                .statusCode(SC_OK)
                .body("status", equalTo(200));
    }

    @Test
    @Disabled("Sumtin is no yes \"╯°□°）╯\"")
    @DisplayName("Hello there status a string")
    void getHelloFlagStatusFails() {
        when().get("/flag/1")
                .then().log().ifValidationFails()
                .statusCode(SC_OK)
                .body("status", equalTo("200"));
    }

    @Test
    void getKenobiFlag() {
        Response response = when().get("/flag/6")
                .then().log().ifValidationFails()
                .statusCode(SC_OK)
                .extract()
                .response();

        assertThat(response.jsonPath().getString("flag"))
                .isEqualTo("${flag_general_kenobi}");
    }


    @ParameterizedTest(name = "#{index} for flagId = {0}")
    @DisplayName("Checking flag composition:")
    @ValueSource(ints = {1, 6})
    void checkFlagComposition(int flagId) {
        Response response = given()
                .contentType("application/json; charset=utf-8")
                .pathParam("flagId", flagId)
                .when()
                .get("/flag/{flagId}")
                .then().log().ifValidationFails()
                .statusCode(SC_OK)
                .extract()
                .response();

        assertThat(response.jsonPath().getString("flag"))
                .matches("^\\$\\{flag_.*}$"); // ${flag_cokolwiek_tutaj}
    }

    @ParameterizedTest(name = "#{index} for flagId = {0}")
    @ValueSource(ints = {0, 2, 3, 4, 5, 7})
    void checkFlagNotFound(int flagId) {
        Response response = given()
                .contentType("application/json; charset=utf-8")
                .pathParam("flagId", flagId)
                .when()
                .get("/flag/{flagId}")
                .then().log().everything()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .extract()
                .response();

        assertThat(response.jsonPath().getString("flag"))
                .isEqualTo("Nope");
    }
}
