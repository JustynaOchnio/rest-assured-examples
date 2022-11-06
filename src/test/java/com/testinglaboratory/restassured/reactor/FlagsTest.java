package com.testinglaboratory.restassured.reactor;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;


public class FlagsTest extends BaseReactorTest {

    @Test
    public void checkSneakyRatFlag() {
        Response response = when()
                .get("/./control_room")
                .andReturn();
        assertThat(response.getBody().jsonPath().getString("message"))
                .contains("${flag_sneaky_rat}");
    }

    @Test
    public void checkCuriousFlag() {
        Response response = when()
                .get("/" + userKey + "/reactor_core")
                .prettyPeek()
                .andReturn();
        assertThat(response.getBody().jsonPath().getString("flag"))
                .contains("${flag_curious_arent_we_");
    }

    @Test
    public void shouldGetReactorInfo() {
        Response response = when()
                .get("/" + userKey + "/reactor_core")
                .prettyPeek()
                .andReturn();
        assertThat(response.getBody().jsonPath().getString("message"))
                .contains(" the core looks fine!");
    }

    @Test
    public void resetProgress(){
        Response response = when()
                .get("/" + userKey + "/reset_progress")
                .andReturn();
        assertThat(response.getBody().jsonPath().getString("message"))
                .isEqualTo("Your reactor is good as new!");
    }

    @Test
    public void shouldBeReset(){
        ReceptionistResponse receptionistResponse = given().body(Map.of("name", randomizeUser()))
                .post("/desk")
                .then().statusCode(201)
                .extract()
                .as(ReceptionistResponse.class);
        when().get(String.format("/%s/reset_progress", receptionistResponse.key))
                .then().statusCode(200)
                .body("message", equalTo("Your reactor is good as new!"));
    }
}
