package com.nicmsaraiva.api.users;

import com.nicmsaraiva.api.base.BaseTest;
import com.nicmsaraiva.api.utils.JsonBuilder;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.nicmsaraiva.api.utils.TestDataGenerator.generateEmail;
import static com.nicmsaraiva.api.utils.TestDataGenerator.generatePassword;
import static com.nicmsaraiva.enums.Endpoints.USERS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UpdateUserTest extends BaseTest {

    @Test
    @DisplayName("PUT /usuarios/{id} - should update user and return 200")
    void shouldUpdateUserSuccessfully() throws Exception {
        String userId = createUser();

        String requestBody = JsonBuilder.from("/update-user.json")
                .with("nome", "Nicolas Updated")
                .with("email", generateEmail())
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro alterado com sucesso"));
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - should create user and return 201 when id does not exist")
    void shouldCreateUserWhenIdDoesNotExist() throws Exception {
        String requestBody = JsonBuilder.from("/update-user.json")
                .with("nome", "Nicolas New")
                .with("email", generateEmail())
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(USERS.getPath() + "/id-inexistente")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .body("_id", notNullValue());
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - should return 400 when email is already in use")
    void shouldReturn400WhenEmailIsAlreadyInUse() throws Exception {
        String existingEmail = generateEmail();

        given()
                .spec(requestSpec)
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
                .spec(requestSpec)
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
    @DisplayName("PUT /usuarios/{id} - should return 400 when email is missing")
    void shouldReturn400WhenEmailIsMissing() throws Exception {
        String userId = createUser();

        String requestBody = JsonBuilder.from("/update-user.json")
                .with("nome", "Nicolas")
                .with("password", generatePassword())
                .with("administrador", "true")
                .without("email")
                .build();

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email é obrigatório"));
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - should return 400 when name is missing")
    void shouldReturn400WhenNameIsMissing() throws Exception {
        String userId = createUser();

        String requestBody = JsonBuilder.from("/update-user.json")
                .with("email", generateEmail())
                .with("password", generatePassword())
                .with("administrador", "true")
                .without("nome")
                .build();

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("nome", equalTo("nome é obrigatório"));
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - should return 400 when password is missing")
    void shouldReturn400WhenPasswordIsMissing() throws Exception {
        String userId = createUser();

        String requestBody = JsonBuilder.from("/update-user.json")
                .with("nome", "Nicolas")
                .with("email", generateEmail())
                .with("administrador", "true")
                .without("password")
                .build();

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("password", equalTo("password é obrigatório"));
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - should return 400 when email is invalid")
    void shouldReturn400WhenEmailIsInvalid() throws Exception {
        String userId = createUser();

        String requestBody = JsonBuilder.from("/update-user.json")
                .with("nome", "Nicolas")
                .with("email", "invalid-email")
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email deve ser um email válido"));
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - should return 400 when administrador is missing")
    void shouldReturn400WhenAdministradorIsMissing() throws Exception {
        String userId = createUser();

        String requestBody = JsonBuilder.from("/update-user.json")
                .with("nome", "Nicolas")
                .with("email", generateEmail())
                .with("password", generatePassword())
                .without("administrador")
                .build();

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("administrador", equalTo("administrador é obrigatório"));
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - should return 400 when name is integer")
    void shouldReturn400WhenNameIsInteger() throws Exception {
        String userId = createUser();

        String requestBody = JsonBuilder.from("/update-user.json")
                .with("nome", 123)
                .with("email", generateEmail())
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("nome", equalTo("nome deve ser uma string"));
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - should return 400 when email is integer")
    void shouldReturn400WhenEmailIsInteger() throws Exception {
        String userId = createUser();

        String requestBody = JsonBuilder.from("/update-user.json")
                .with("nome", "Nicolas")
                .with("email", 123)
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("email", equalTo("email deve ser uma string"));
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - should return 400 when password is integer")
    void shouldReturn400WhenPasswordIsInteger() throws Exception {
        String userId = createUser();

        String requestBody = JsonBuilder.from("/update-user.json")
                .with("nome", "Nicolas")
                .with("email", generateEmail())
                .with("password", 123)
                .with("administrador", "true")
                .build();

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("password", equalTo("password deve ser uma string"));
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - should return 400 when administrador is integer")
    void shouldReturn400WhenAdministradorIsInteger() throws Exception {
        String userId = createUser();

        String requestBody = JsonBuilder.from("/update-user.json")
                .with("nome", "Nicolas")
                .with("email", generateEmail())
                .with("password", generatePassword())
                .with("administrador", 123)
                .build();

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("administrador", equalTo("administrador deve ser 'true' ou 'false'"));
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - should keep updated data after update")
    void shouldKeepUpdatedDataAfterUpdate() throws Exception {
        String userId = createUser();
        String newEmail = generateEmail();

        String requestBody = JsonBuilder.from("/update-user.json")
                .with("nome", "Nicolas Updated")
                .with("email", newEmail)
                .with("password", generatePassword())
                .with("administrador", "true")
                .build();

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .put(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_OK);

        given()
                .spec(requestSpec)
                .when()
                .get(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("nome", equalTo("Nicolas Updated"))
                .body("email", equalTo(newEmail));
    }
}