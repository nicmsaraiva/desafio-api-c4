package com.nicmsaraiva.api.users;

import com.nicmsaraiva.api.base.BaseTest;
import com.nicmsaraiva.api.utils.TestDataGenerator;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.nicmsaraiva.enums.Endpoints.USERS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetUserTest extends BaseTest {

    private static String token;

    @BeforeAll
    static void auth() throws Exception {
        token = getAuthToken();
    }

    @Test
    @DisplayName("GET /usuarios - should return all users with status 200")
    void shouldReturnAllUsers() {
        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
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
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
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
        String email = TestDataGenerator.generateEmail();
        createUser("Nick", email);

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
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
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
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
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .queryParam("nome", TestDataGenerator.generateAlphanumeric(10))
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
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
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
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .get(USERS.getPath() + "/" + TestDataGenerator.generateAlphanumeric(16))
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Usuário não encontrado"));
    }

    @Test
    @DisplayName("GET /usuarios/{id} - should return 400 when id has invalid length")
    void shouldReturn400WhenIdHasInvalidLength() {
        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .get(USERS.getPath() + "/" + TestDataGenerator.generateAlphanumeric(15))
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("id", equalTo("id deve ter exatamente 16 caracteres alfanuméricos"));
    }
}