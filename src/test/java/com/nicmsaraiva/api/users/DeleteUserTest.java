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

public class DeleteUserTest extends BaseTest {

    private static String token;

    @BeforeAll
    static void auth() throws Exception {
        token = getAuthToken();
    }

    @Test
    @DisplayName("Delete user with success, then return status 200 - OK")
    void deleteUserWithSuccess_thenReturnStatus200() throws Exception {
        String userId = createUser();

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .delete(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"));
    }

    @Test
    @DisplayName("Delete user with invalid id, then return status 400 - Bad Request")
    void deleteUserWithInvalidId_thenReturnStatus400() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .delete(USERS.getPath() + "/id-invalido")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Nenhum registro excluído"));
    }

    @Test
    @DisplayName("Delete user already deleted, then return status 400 - Bad Request")
    void deleteUserAlreadyDeleted_thenReturnStatus400() throws Exception {
        String userId = createUser();

        // First delete - user exists and is deleted successfully
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .delete(USERS.getPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Registro excluído com sucesso"));

        // Second delete - user does not exist anymore
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .delete(USERS.getPath() + "/" + userId)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Nenhum registro excluído"));
    }
}