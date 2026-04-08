package com.nicmsaraiva.api.users;

import com.nicmsaraiva.api.base.BaseTest;
import com.nicmsaraiva.api.utils.JsonBuilder;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.nicmsaraiva.api.utils.TestDataGenerator.generateEmail;
import static com.nicmsaraiva.api.utils.TestDataGenerator.generatePassword;
import static com.nicmsaraiva.enums.Endpoints.USERS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest extends BaseTest {

    private static String token;

    @BeforeAll
    static void auth() throws Exception {
        token = getAuthToken();
    }

    @Test
    @DisplayName("POST /usuarios - should create user and return 201")
    void shouldCreateUserSuccessfully() throws Exception {
        String requestBody = JsonBuilder.from("/create-user.json")
                .with("nome", "Nicolas")
                .with("email", generateEmail())
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"));
    }

    @Test
    @DisplayName("POST /usuarios - should return 400 when email is missing")
    void shouldReturn400WhenEmailIsMissing() throws Exception {
        String requestBody = JsonBuilder.from("/create-user.json")
                .with("nome", "Nicolas")
                .with("password", generatePassword())
                .with("administrador", "true")
                .without("email")
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email é obrigatório"));
    }

    @Test
    @DisplayName("POST /usuarios - should return 400 when email is invalid")
    void shouldReturn400WhenEmailIsInvalid() throws Exception {
        String requestBody = JsonBuilder.from("/create-user.json")
                .with("nome", "Nicolas")
                .with("email", "invalid-email")
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email deve ser um email válido"));
    }

    @Test
    @DisplayName("POST /usuarios - should return 400 when password is missing")
    void shouldReturn400WhenPasswordIsMissing() throws Exception {
        String requestBody = JsonBuilder.from("/create-user.json")
                .with("nome", "Nicolas")
                .with("email", generateEmail())
                .with("administrador", "true")
                .without("password")
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("password", equalTo("password é obrigatório"));
    }

    @Test
    @DisplayName("POST /usuarios - should return 400 when name is missing")
    void shouldReturn400WhenNameIsMissing() throws Exception {
        String requestBody = JsonBuilder.from("/create-user.json")
                .with("email", generateEmail())
                .with("password", generatePassword())
                .with("administrador", "true")
                .without("nome")
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("nome", equalTo("nome é obrigatório"));
    }

    @Test
    @DisplayName("POST /usuarios - should return 400 when administrador is missing")
    void shouldReturn400WhenAdministradorIsMissing() throws Exception {
        String requestBody = JsonBuilder.from("/create-user.json")
                .with("nome", "Nicolas")
                .with("email", generateEmail())
                .with("password", generatePassword())
                .without("administrador")
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("administrador", equalTo("administrador é obrigatório"));
    }

    @Test
    @DisplayName("POST /usuarios - should return 400 when name is integer")
    void shouldReturn400WhenNameIsInteger() throws Exception {
        String requestBody = JsonBuilder.from("/create-user.json")
                .with("nome", 123)
                .with("email", generateEmail())
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("nome", equalTo("nome deve ser uma string"));
    }

    @Test
    @DisplayName("POST /usuarios - should return 400 when email is integer")
    void shouldReturn400WhenEmailIsInteger() throws Exception {
        String requestBody = JsonBuilder.from("/create-user.json")
                .with("nome", "Nicolas")
                .with("email", 123)
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email deve ser uma string"));
    }

    @Test
    @DisplayName("POST /usuarios - should return 400 when password is integer")
    void shouldReturn400WhenPasswordIsInteger() throws Exception {
        String requestBody = JsonBuilder.from("/create-user.json")
                .with("nome", "Nicolas")
                .with("email", generateEmail())
                .with("password", 123)
                .with("administrador", "true")
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("password", equalTo("password deve ser uma string"));
    }

    @Test
    @DisplayName("POST /usuarios - should return 400 when administrador is integer")
    void shouldReturn400WhenAdministradorIsInteger() throws Exception {
        String requestBody = JsonBuilder.from("/create-user.json")
                .with("nome", "Nicolas")
                .with("email", generateEmail())
                .with("password", generatePassword())
                .with("administrador", 123)
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("administrador", equalTo("administrador deve ser 'true' ou 'false'"));
    }
}