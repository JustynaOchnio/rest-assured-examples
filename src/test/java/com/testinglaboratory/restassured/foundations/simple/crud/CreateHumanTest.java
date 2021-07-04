package com.testinglaboratory.restassured.foundations.simple.crud;

import com.github.javafaker.Faker;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.in;

@Slf4j
class CreateHumanTest {

    @Test
    void createHuman() {
        Faker fake = new Faker(new Locale("PL_pl"));
        String firstName = fake.name().firstName();
        String lastName = fake.name().lastName();
        JSONObject human = new JSONObject();
        human.put("first_name", firstName);
        human.put("last_name", lastName);
        given()
                .header("Content-Type", "application/json")
                .body(human)
                .when()
                .post("/human")
                .then()
                .statusCode(in(List.of(201, 307)))
                .log().everything();
    }

    @Test
    void createHumanValidatingResponse() {
        Faker fake = new Faker(new Locale("PL_pl"));
        String firstName = fake.name().firstName();
        String lastName = fake.name().lastName();
        JsonObject human = new JsonObject();
        human.addProperty("first_name", firstName);
        human.addProperty("last_name", lastName);
        log.info(human.toString());
        given()
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .body(human)
                .post("/human/")
                .then()
                .log().everything()
                .assertThat()
                .body(containsString(firstName))
                .body(containsString(lastName));

    }

}
