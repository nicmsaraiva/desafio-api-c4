package com.nicmsaraiva.api.users;

import com.nicmsaraiva.api.base.BaseTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.nicmsaraiva.enums.Endpoints.USERS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteUserTest extends BaseTest {

    private static String token;

    @BeforeAll
    static void auth() throws Exception {
        token = getAuthToken();
    }

    @Test
    @DisplayName("DELETE /usuarios - should delete user and return 200")
    void shouldDeleteUserSuccessfully() throws Exception {
        String userId = createUser();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .delete(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"));
    }

    @Test
    @DisplayName("DELETE /usuarios - should return 200 with no record deleted when id is invalid")
    void shouldReturn200WithNoRecordWhenIdIsInvalid() {
        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .delete(USERS.getPath() + "/id-invalido")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Nenhum registro excluído"));
    }

    @Test
    @DisplayName("DELETE /usuarios - should return 200 with no record deleted when user is already deleted")
    void shouldReturn200WithNoRecordWhenUserIsAlreadyDeleted() throws Exception {
        String userId = createUser();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .delete(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"));

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .delete(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Nenhum registro excluído"));
    }
}