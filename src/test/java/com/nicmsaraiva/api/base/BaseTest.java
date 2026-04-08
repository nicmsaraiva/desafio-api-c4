package com.nicmsaraiva.api.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicmsaraiva.api.utils.JsonBuilder;
import com.nicmsaraiva.config.APIConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.nicmsaraiva.api.utils.TestDataGenerator.generateEmail;
import static com.nicmsaraiva.api.utils.TestDataGenerator.generatePassword;
import static com.nicmsaraiva.enums.Endpoints.USERS;
import static io.restassured.RestAssured.given;

public class BaseTest extends APIConfig {

    private static String cachedToken;
    protected static RequestSpecification requestSpec;
    private final List<String> createdUserIds = new ArrayList<>();

    @BeforeAll
    static void setup() throws Exception {
        APIConfig.configure();

        requestSpec = given()
                .contentType(ContentType.JSON)
                .auth().oauth2(getAuthToken())
                .log().ifValidationFails();
    }

    @AfterEach
    void cleanup() {
        createdUserIds.forEach(userId ->
                given()
                        .spec(requestSpec)
                        .when()
                        .delete(USERS.getPath() + "/" + userId)
        );
        createdUserIds.clear();
    }

    protected static String getAuthToken() throws Exception {
        if (cachedToken != null) {
            return cachedToken;
        }

        Properties props = APIConfig.loadProperties();
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

        cachedToken = new ObjectMapper()
                .readTree(response.body())
                .get("authorization")
                .asText();

        return cachedToken;
    }

    protected String createUser() throws Exception {
        return createUser("Nicolas", generateEmail());
    }

    protected String createUser(String name) throws Exception {
        return createUser(name, generateEmail());
    }

    protected String createUser(String name, String email) throws Exception {
        String requestBody = JsonBuilder.from("/create-user.json")
                .with("nome", name)
                .with("email", email)
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        String userId = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .path("_id");

        createdUserIds.add(userId);
        return userId;
    }
}