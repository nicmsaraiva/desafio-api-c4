package com.nicmsaraiva.api.base;

import com.nicmsaraiva.config.ApiConfig;
import com.nicmsaraiva.api.utils.JsonBuilder;
import com.nicmsaraiva.enums.Endpoints;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;

import static com.nicmsaraiva.api.utils.Generator.generateEmail;
import static com.nicmsaraiva.api.utils.Generator.generatePassword;
import static com.nicmsaraiva.enums.Endpoints.USERS;
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

    protected static String createUser() throws Exception {
        String requestBody = JsonBuilder.from("/create-user.json")
                .with("nome", "Nicolas")
                .with("email", generateEmail())
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(getAuthToken())
                .body(requestBody)
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .path("_id");
    }

    protected static String createUser(String name) throws Exception {
        String requestBody = JsonBuilder.from("/create-user.json")
                .with("nome", name)
                .with("email", generateEmail())
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(getAuthToken())
                .body(requestBody)
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .path("_id");
    }

    protected static String createUser(String name, String email) throws Exception {
        String requestBody = JsonBuilder.from("/create-user.json")
                .with("nome", name)
                .with("email", email)
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(getAuthToken())
                .body(requestBody)
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .path("_id");
    }
}