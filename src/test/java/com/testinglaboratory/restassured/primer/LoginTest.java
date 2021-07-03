package com.testinglaboratory.restassured.primer;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

class LoginTest {
    private static final Faker faker = new Faker();
    private static User user;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:8082";
        RestAssured.basePath = "/challenge/primer";
        user = new User(faker.name().username(), faker.internet().password());
        Response response = registerUser(user);
        response.then().statusCode(SC_CREATED);
    }

    @Test
    void loginValid() {
        given()
                .body(user)
                .when()
                .post("/login")
                .then()
                .log().everything()
                .statusCode(SC_ACCEPTED);
    }

    @Test
    void loginInvalid() {
        user.username += faker.number().digits(10);
        given()
                .body(user)
                .when()
                .post("/login")
                .then()
                .log().everything()
                .statusCode(SC_UNAUTHORIZED)
                .body("message",
                        equalTo("Failed to login. Wrong username or password."))
                .body("flag",
                        equalTo("${flag_naughty_aint_ya}"));
    }


    private static Response registerUser(User user) {
        return given()
                .body(user)
                .post("/register")
                .then().log().everything()
                .extract()
                .response();
    }
}
