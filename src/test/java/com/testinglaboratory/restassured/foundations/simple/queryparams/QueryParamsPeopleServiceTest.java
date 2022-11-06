package com.testinglaboratory.restassured.foundations.simple.queryparams;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class QueryParamsPeopleServiceTest {
    @DisplayName("Shameful test")
    @Tag("Known Issue")
    @Test
    @Disabled("JIRA_TICKET-76531")
    public void shouldGreetPersonWithFirstNameAndLastName() {
        given().queryParam("first_name", "Andrzej")
                .queryParam("last_name", "Kowalski")
                .when()
                .log().method().log().parameters()
                .get("/query_params")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("Greeting", equalTo("Hello, Andrzej Kowalski!"))
                .log().ifValidationFails();
    }


    @Test
    public void shouldGreetPersonWithFullName() {
        given().queryParam("first_name", "Tomasz")
                .queryParam("last_name", "Kowalski")
                .when()
                .log().method().log().parameters()
                .get("/query_params")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("Greeting", equalTo("Hello, Tomasz Kowalski!"))
                .log().ifValidationFails();
    }

    @Test
    public void shouldOverwriteDuplicatedParam() {
        given().queryParam("first_name", "Stefan")
                .queryParam("last_name", "Jaracz")
                .queryParam("last_name", "Madafaka")
                .when()
                .get("/query_params")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("Greeting", equalTo("Hello, Stefan Madafaka!"));
    }


    @Test
    public void shouldIncludeProvidedMiddleNameWithGreeting() {
        Faker fake = new Faker(new Locale("PL_pl"));
        String firstName = fake.name().firstName();
        String middleName = fake.name().firstName();
        String lastName = fake.name().lastName();
        log.info(firstName);
        log.info(middleName);
        log.info(lastName);
        given().queryParam("first_name", firstName)
                .queryParam("last_name", lastName)
                .queryParam("middle_name", middleName)
                .when()
                .get("/query_params")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("Greeting", equalTo(
                        String.format("Hello, %s %s %s!", firstName, middleName, lastName)))
                .log().everything();
    }

    @Test
    public void shouldReturnListOfAllPeople() {
        Response response = when()
                .get("/get_all_people")
                .andReturn();
        Map<String, Map<String, String>> people = response.body().jsonPath().getMap(".");
        log.info(String.valueOf(people));
        assertThat(people)
                .as("People of this beautiful country")
                .hasSizeGreaterThanOrEqualTo(1000);
    }
}
