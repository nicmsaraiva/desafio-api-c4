package com.nicmsaraiva.api.users;

import com.nicmsaraiva.api.base.BaseTest;
import com.nicmsaraiva.api.utils.Generator;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.nicmsaraiva.enums.Endpoints.USERS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetUserTest extends BaseTest {

    public static String token;

    @BeforeAll
    public static void auth() throws Exception {
        token = BaseTest.getAuthToken();
    }
    @Test
    @DisplayName("Get all users with success, then return status 200 - OK")
    void getAllUsersWithSuccess_thenReturnStatus200() {
        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .get(USERS.getPath())
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("quantidade", greaterThanOrEqualTo(1))
                .body("usuarios", not(empty()))
                .body("usuarios[0]._id", notNullValue())
                .body("usuarios[0].nome", notNullValue())
                .body("usuarios[0].email", notNullValue());
    }

    @Test
    @DisplayName("Get users filtered by name, then return status 200 - OK")
    void getUsersFilteredByName_thenReturnStatus200() {
        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .queryParam("nome", "Nicolas Saraiva")
                .when()
                .get(USERS.getPath())
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("usuarios.nome", hasItem("Nicolas Saraiva"));
    }

    @Test
    @DisplayName("Get users filtered by email, then return status 200 - OK")
    void getUsersFilteredByEmail_thenReturnStatus200() {
        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .queryParam("email", "nicolas@qa.com.br")
                .when()
                .get(USERS.getPath())
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("quantidade", equalTo(1))
                .body("usuarios[0].nome", equalTo("Nicolas Saraiva"));
    }

    @Test
    @DisplayName("Get users filtered by administrador, then return status 200 - OK")
    void getUsersFilteredByAdministrador_thenReturnStatus200() {
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
    @DisplayName("Get users with no results, then return empty list")
    void getUsersWithNoResults_thenReturnEmptyList() {
        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .queryParam("nome", "usuario-que-nao-existe-xyz")
                .when()
                .get(USERS.getPath())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("quantidade", equalTo(0))
                .body("usuarios", empty());
    }

    @Test
    @DisplayName("Get user by id with success, then return status 200 - OK")
    void getUserByIdWithSuccess_thenReturnStatus200() {
        String userId = "0qFmNK2jYpUonRkX";
        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .get(USERS.getPath() + "/" + userId)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("_id", equalTo(userId))
                .body("nome", notNullValue())
                .body("email", notNullValue())
                .body("administrador", notNullValue());
    }

    @Test
    @DisplayName("Get user by invalid id, then return status 400 - Bad Request")
    void getUserByInvalidId_thenReturnStatus400() {
        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .get(USERS.getPath() + "/" + Generator.generateAlphanumeric(16))
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Usuário não encontrado"));
    }

    @Test
    @DisplayName("Get user by invalid length id, then return status 400 - Bad Request")
    void getUserByInvalidLengthId_thenReturnStatus400() {
        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .get(USERS.getPath() + "/" + Generator.generateAlphanumeric(15))
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("id", equalTo("id deve ter exatamente 16 caracteres alfanuméricos"));
    }
}
