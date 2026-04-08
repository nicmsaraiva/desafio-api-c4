package com.nicmsaraiva.api.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicmsaraiva.api.utils.JsonBuilder;
import com.nicmsaraiva.config.ApiConfig;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

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
        Properties props = ApiConfig.loadProperties();
        String baseUri = props.getProperty("base.uri");

        String body = JsonBuilder.from("/login.json").build();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUri + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        return new ObjectMapper()
                .readTree(response.body())
                .get("authorization")
                .asText();
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