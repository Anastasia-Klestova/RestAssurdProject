package ru.academits.reqres;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DelayedReactionTest {

    @Test
    public void delayedReactionParamTest() {
        ValidatableResponse response = RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/data ")
                .then();
        String reportToHeader = response.extract().header("Report-To");
        System.out.println("Report-To: " + reportToHeader);
        assertNotNull(reportToHeader);
    }

    @Test
    public void statusCodeTest() {
        Response response = RestAssured
                .get("https://reqres.in/api/users?delay=3")
                .andReturn();
        response.prettyPrint();
        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
        assertEquals(200, response.statusCode(), "Unexpected status code");

    }

    @Test
    public void testResponseTime() {
        ValidatableResponse response = RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/?delay")
                .then()
                .assertThat()
                .time(lessThanOrEqualTo(7000L));

    }

    @Test
    public void testResponseBody() {
        ValidatableResponse response = RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/users?delay")
                .then()
                .body("data.id", notNullValue())
                .body("data.email", notNullValue())
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue());
    }

    @Test
    public void testInvalidRequest() {
        ValidatableResponse response = RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/?delay=3")
                .then()
                .statusCode(404);
    }
}
