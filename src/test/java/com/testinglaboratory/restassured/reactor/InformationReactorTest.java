package com.testinglaboratory.restassured.reactor;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

public class InformationReactorTest extends BaseReactorTest{

    @Test
    void checkInformation(){
        InformationMessage information = when()
                .get("/information")
                .as(InformationMessage.class);
        assertThat(information.message).isEqualToIgnoringWhitespace("You are the Tech Commander of RBMK reactor power plant." +
                "Your task is to perform the reactor test. " +
                "Bring the power level above 1000 but below 1500 and keep the reactor Operational. " +
                "Use /{key}/control_room/analysis to peek at reactor core. " +
                "Use /{key}/control_room to see full info about the reactor. " +
                "Check in at the /desk to get your key to control room. " +
                "Put in fuel rods or pull out control rods to raise the power. " +
                "Put in control rods or pull out fuel rods to decrease the power. " +
                "There are 13 flags to find. Good luck Commander.");
    }
}
