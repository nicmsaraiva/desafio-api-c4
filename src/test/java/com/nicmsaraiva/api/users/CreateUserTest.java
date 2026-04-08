package com.nicmsaraiva.api.users;

import com.nicmsaraiva.api.base.BaseTest;
import com.nicmsaraiva.api.utils.JsonBuilder;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.nicmsaraiva.api.utils.Generator.generateEmail;
import static com.nicmsaraiva.api.utils.Generator.generatePassword;
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
    @DisplayName("Create user with success, then return status 200 - Created")
    public void createUserTestWithSuccess_thenReturnStatus200() throws Exception {
        String requestBody =
                JsonBuilder.from("/create-user.json")
                        .with("nome", "Nicolas")
                        .with("email", generateEmail())
                        .with("password", generatePassword())
                        .with("administrador", "true")
                        .build();
        given()
                .log().all()
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
    @DisplayName("Create user without email, then return status 400 - Bad Request")
    void createUserWithoutEmail_thenReturnStatus400() throws Exception {
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
    @DisplayName("Create user with invalid email, then return status 400 - Bad Request")
    void createUserWithInvalidEmail_thenReturnStatus400() throws Exception {
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
    @DisplayName("Create user without password, then return status 400 - Bad Request")
    void createUserWithoutPassword_thenReturnStatus400() throws Exception {
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
    @DisplayName("Create user without name, then return status 400 - Bad Request")
    void createUserWithoutName_thenReturnStatus400() throws Exception {
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
    @DisplayName("Create user without name, then return status 400 - Bad Request")
    void createUserWithoutAdministrador_thenReturnStatus400() throws Exception {
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
    @DisplayName("Create user with integer name, then return status 400 - Bad Request")
    void createUserWithIntegerName_thenReturnStatus400() throws Exception {
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
    @DisplayName("Create user with integer email, then return status 400 - Bad Request")
    void createUserWithIntegerEmail_thenReturnStatus400() throws Exception {
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
    @DisplayName("Create user with integer password, then return status 400 - Bad Request")
    void createUserWithIntegerPassword_thenReturnStatus400() throws Exception {
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
    @DisplayName("Create user with integer administrador, then return status 400 - Bad Request")
    void createUserWithIntegerAdministrador_thenReturnStatus400() throws Exception {
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
