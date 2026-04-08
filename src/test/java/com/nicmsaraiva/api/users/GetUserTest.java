package com.nicmsaraiva.api.users;

import com.nicmsaraiva.api.base.BaseTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.nicmsaraiva.api.utils.TestDataGenerator.generateAlphanumeric;
import static com.nicmsaraiva.api.utils.TestDataGenerator.generateEmail;
import static com.nicmsaraiva.enums.Endpoints.USERS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetUserTest extends BaseTest {

    @Test
    @DisplayName("GET /usuarios - should return all users with status 200")
    void shouldReturnAllUsers() {
        given()
                .spec(requestSpec)
                .when()
                .get(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("quantidade", greaterThanOrEqualTo(1))
                .body("usuarios", not(empty()))
                .body("usuarios[0]._id", notNullValue())
                .body("usuarios[0].nome", notNullValue())
                .body("usuarios[0].email", notNullValue());
    }

    @Test
    @DisplayName("GET /usuarios - should return users filtered by name")
    void shouldReturnUsersFilteredByName() throws Exception {
        createUser("Nick");

        given()
                .spec(requestSpec)
                .queryParam("nome", "Nick")
                .when()
                .get(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("quantidade", greaterThanOrEqualTo(1))
                .body("usuarios.nome", hasItem("Nick"));
    }

    @Test
    @DisplayName("GET /usuarios - should return users filtered by email")
    void shouldReturnUsersFilteredByEmail() throws Exception {
        String email = generateEmail();
        createUser("Nick", email);

        given()
                .spec(requestSpec)
                .queryParam("email", email)
                .when()
                .get(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("quantidade", equalTo(1))
                .body("usuarios[0].nome", equalTo("Nick"));
    }

    @Test
    @DisplayName("GET /usuarios - should return users filtered by administrador")
    void shouldReturnUsersFilteredByAdministrador() throws Exception {
        createUser();

        given()
                .spec(requestSpec)
                .queryParam("administrador", "true")
                .when()
                .get(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("usuarios.administrador", everyItem(equalTo("true")));
    }

    @Test
    @DisplayName("GET /usuarios - should return empty list when no users match")
    void shouldReturnEmptyListWhenNoUsersMatch() {
        given()
                .spec(requestSpec)
                .queryParam("nome", generateAlphanumeric(10))
                .when()
                .get(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("quantidade", equalTo(0))
                .body("usuarios", empty());
    }

    @Test
    @DisplayName("GET /usuarios/{id} - should return user by id with status 200")
    void shouldReturnUserById() throws Exception {
        String userId = createUser();

        given()
                .spec(requestSpec)
                .when()
                .get(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("_id", equalTo(userId))
                .body("nome", notNullValue())
                .body("email", notNullValue())
                .body("administrador", notNullValue());
    }

    @Test
    @DisplayName("GET /usuarios/{id} - should return 400 when id does not exist")
    void shouldReturn400WhenIdDoesNotExist() {
        given()
                .spec(requestSpec)
                .when()
                .get(USERS.getPath() + "/" + generateAlphanumeric(16))
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Usuário não encontrado"));
    }

    @Test
    @DisplayName("GET /usuarios/{id} - should return 400 when id has invalid length")
    void shouldReturn400WhenIdHasInvalidLength() {
        given()
                .spec(requestSpec)
                .when()
                .get(USERS.getPath() + "/" + generateAlphanumeric(15))
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("id", equalTo("id deve ter exatamente 16 caracteres alfanuméricos"));
    }

    @Test
    @DisplayName("GET /usuarios - should return users filtered by multiple params")
    void shouldReturnUsersFilteredByMultipleParams() throws Exception {
        String email = generateEmail();
        createUser("Nick", email);

        given()
                .spec(requestSpec)
                .queryParam("nome", "Nick")
                .queryParam("email", email)
                .queryParam("administrador", "true")
                .when()
                .get(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("quantidade", equalTo(1))
                .body("usuarios[0].nome", equalTo("Nick"))
                .body("usuarios[0].email", equalTo(email));
    }

    @Test
    @DisplayName("GET /usuarios - should return empty list when administrador filter has no match")
    void shouldReturnEmptyWhenAdministradorFilterHasNoMatch() {
        given()
                .spec(requestSpec)
                .queryParam("administrador", "false")
                .queryParam("nome", generateAlphanumeric(10))
                .when()
                .get(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("quantidade", equalTo(0))
                .body("usuarios", empty());
    }

    @Test
    @DisplayName("GET /usuarios/{id} - should return correct fields for created user")
    void shouldReturnCorrectFieldsForCreatedUser() throws Exception {
        String email = generateEmail();
        String userId = createUser("Nick Verified", email);

        given()
                .spec(requestSpec)
                .when()
                .get(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("_id", equalTo(userId))
                .body("nome", equalTo("Nick Verified"))
                .body("email", equalTo(email))
                .body("administrador", equalTo("true"))
                .body("password", notNullValue());
    }

    @Test
    @DisplayName("GET /usuarios/{id} - should return 400 when id is empty")
    void shouldReturn400WhenIdIsEmpty() {
        given()
                .spec(requestSpec)
                .when()
                .get(USERS.getPath() + "/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("quantidade", notNullValue());
    }
}