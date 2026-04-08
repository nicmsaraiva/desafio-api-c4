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
import static org.hamcrest.Matchers.notNullValue;

public class UpdateUserTest extends BaseTest {

    private static String token;

    @BeforeAll
    static void auth() throws Exception {
        token = getAuthToken();
    }

    @Test
    @DisplayName("Update user with success, then return status 200 - OK")
    void updateUserWithSuccess_thenReturnStatus200() throws Exception {
        String userId = createUser();

        String requestBody = JsonBuilder.from("/update-user.json")
                .with("nome", "Nicolas Updated")
                .with("email", generateEmail())
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro alterado com sucesso"));
    }

    @Test
    @DisplayName("Update user with non-existent id creates new user, then return status 201 - Created")
    void updateUserWithNonExistentId_thenReturnStatus201() throws Exception {
        String requestBody = JsonBuilder.from("/update-user.json")
                .with("nome", "Nicolas New")
                .with("email", generateEmail())
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .put(USERS.getPath() + "/id-inexistente")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .body("_id", notNullValue());
    }

    @Test
    @DisplayName("Update user with duplicate email, then return status 400 - Bad Request")
    void updateUserWithDuplicateEmail_thenReturnStatus400() throws Exception {
        String existingEmail = generateEmail();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(JsonBuilder.from("/update-user.json")
                        .with("nome", "Nicolas")
                        .with("email", existingEmail)
                        .with("password", generatePassword())
                        .with("administrador", "true")
                        .build())
                .when()
                .post(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        String userId = createUser();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(JsonBuilder.from("/update-user.json")
                        .with("nome", "Nicolas")
                        .with("email", existingEmail)
                        .with("password", generatePassword())
                        .with("administrador", "true")
                        .build())
                .when()
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Este email já está sendo usado"));
    }

    @Test
    @DisplayName("Update user without email, then return status 400 - Bad Request")
    void updateUserWithoutEmail_thenReturnStatus400() throws Exception {
        String userId = createUser();

        String requestBody = JsonBuilder.from("/update-user.json")
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
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email é obrigatório"));
    }

    @Test
    @DisplayName("Update user without name, then return status 400 - Bad Request")
    void updateUserWithoutName_thenReturnStatus400() throws Exception {
        String userId = createUser();

        String requestBody = JsonBuilder.from("/update-user.json")
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
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("nome", equalTo("nome é obrigatório"));
    }

    @Test
    @DisplayName("Update user without password, then return status 400 - Bad Request")
    void updateUserWithoutPassword_thenReturnStatus400() throws Exception {
        String userId = createUser();

        String requestBody = JsonBuilder.from("/update-user.json")
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
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("password", equalTo("password é obrigatório"));
    }
}