package com.testinglaboratory.restassured.primer.lifecyclefiddle;

import com.github.javafaker.Faker;
import com.testinglaboratory.restassured.primer.User;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationTestJunitLifecyclePerClassTest {
    private static final Faker faker = new Faker(new Locale("PL_pl"));

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:8082";
        RestAssured.basePath = "/challenge/primer";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build();
    }

    @Test
    void registerUserTest() {
        String username = String.format("%s.%s", faker.name().firstName(), faker.name().username());
        String password = faker.internet().password();

        Response response = given()
                .body(
                        Map.of(
                                "username", username,
                                "password", password
                        )
                )
                .post("/register")
                .then().log().everything()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .response();

        assertThat(response.jsonPath().getString("message"))
                .as("Confirmation that user has been successfully registered")
                .isEqualTo(String.format("User %s registered", username))
                .matches("User .* registered");
        JsonPathConfig jsonPathConfig = new JsonPathConfig().charset("utf-8");
        assertThat(response.jsonPath(jsonPathConfig).getString("key"))
                .as("Generated UUID composition")
                .matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
    }


    @Test
    void doubleRegistrationFlag() {
        User user = new User(faker.name().username(), faker.internet().password());

        Response firstRegistration = registerUser(user);
        firstRegistration.then().statusCode(SC_CREATED);
        Response secondRegistration = registerUser(user);
        secondRegistration.then().statusCode(SC_BAD_REQUEST)
                .body(
                        "message", equalTo("You are already registered in the Primer Challenge!"))
                .body(
                        "flag", equalTo("${flag_im_still_here_captain}"));
    }

    @Test
    void doubleRegistrationFlagAnotherStyle() {
        User user = new User(faker.name().username(), faker.internet().password());

        registerUser(user, SC_CREATED);
        registerUser(user, SC_BAD_REQUEST)
                .body(
                        "message", equalTo("You are already registered in the Primer Challenge!"))
                .body(
                        "flag", equalTo("${flag_im_still_here_captain}"));
    }

    private Response registerUser(User user) {
        return given()
                .body(user)
                .post("/register")
                .then().log().everything()
                .extract()
                .response();
    }

    private ValidatableResponse registerUser(User user, int statusCode) {
        return given()
                .body(user)
                .post("/register")
                .then().log().everything()
                .statusCode(statusCode)
                .extract().response()
                .then();
    }
}
