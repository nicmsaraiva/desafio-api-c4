package com.nicmsaraiva.api.base;

import com.nicmsaraiva.config.ApiConfig;
import com.nicmsaraiva.api.utils.JsonBuilder;
import com.nicmsaraiva.enums.Endpoints;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class BaseTest extends ApiConfig {

    @BeforeAll
    static void setup() {
        ApiConfig.configure();
    }

    protected static String getAuthToken() throws Exception {
        String requestBody = JsonBuilder
                .from("/login.json")
                .build();

        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(Endpoints.LOGIN.getPath())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("authorization");
    }
}