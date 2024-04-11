package com.yandex.spint_7;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

public class TestCourier extends BaseTest {

    @Step
    @Test
    public void testCreateCourierWithoutData() {
        given().contentType(ContentType.JSON)
                .post("/api/v1/courier").then()
                .assertThat()
                .statusCode(BAD_REQUEST_400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"),
                        "code", BAD_REQUEST_400);
    }

    @Step
    @Test
    public void testCreateCourierWithMissingPassword() {
        given().contentType(ContentType.JSON)
                .body(new JSONObject().put("login", COURIER_LOGIN).toString())
                .post("/api/v1/courier").then()
                .assertThat()
                .statusCode(BAD_REQUEST_400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"),
                        "code", BAD_REQUEST_400);
    }

    @Step
    @Test
    public void testCreateCourierWithMissingLogin() {
        given().contentType(ContentType.JSON)
                .body(new JSONObject().put("password", COURIER_PASSWORD).toString())
                .post("/api/v1/courier").then()
                .assertThat()
                .statusCode(BAD_REQUEST_400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"),
                        "code", BAD_REQUEST_400);
    }

    @Step
    @Test
    public void testLoginWithMissingPassword() {
        given().contentType(ContentType.JSON)
                .body(new JSONObject().put("login", COURIER_LOGIN).toString())
                .post("/api/v1/courier/login").then()
                .assertThat()
                .statusCode(BAD_REQUEST_400)
                .body("message", equalTo("Недостаточно данных для входа"),
                        "code", BAD_REQUEST_400);
    }

    @Step
    @Test
    public void testLoginWithMissingLogin() {
        given().contentType(ContentType.JSON)
                .body(new JSONObject().put("password", COURIER_PASSWORD).toString())
                .post("/api/v1/courier/login").then()
                .assertThat()
                .statusCode(BAD_REQUEST_400)
                .body("message", equalTo("Недостаточно данных для входа"),
                        "code", BAD_REQUEST_400);
    }

    @Step
    @Test
    public void testLoginWithNotExistingLogin() {
        given().contentType(ContentType.JSON)
                .body(new JSONObject()
                        .put("login", COURIER_LOGIN+"_boo")
                        .put("password", COURIER_PASSWORD).toString())
                .post("/api/v1/courier/login").then()
                .assertThat()
                .statusCode(NOT_FOUND_404)
                .body("message", equalTo("Учетная запись не найдена"),
                        "code", NOT_FOUND_404);
    }

    @Step
    @Test
    public void testDeleteCourierWithMissingData() {
        given().contentType(ContentType.JSON)
                .delete("/api/v1/courier/").then()
                .assertThat()
                .statusCode(NOT_FOUND_404)
                .body("message", equalTo("Not Found."),
                        "code", NOT_FOUND_404);
    }

    @Step
    @Test
    public void testDeleteCourierWithBadID() {
        given().contentType(ContentType.JSON)
                .delete("/api/v1/courier/asd").then()
                .assertThat()
                .statusCode(ERR_500)
                .body("message", startsWith("invalid input syntax for type integer"),
                        "code", ERR_500);
    }

    @Step
    @Test
    public void testDeleteCourierWithNotExistingID() {
        given().contentType(ContentType.JSON)
                .delete("/api/v1/courier/0").then()
                .assertThat()
                .statusCode(NOT_FOUND_404)
                .body("message", equalTo("Курьера с таким id нет."),
                        "code", NOT_FOUND_404);
    }

    @Step
    @Test
    public void testCreateCourierHappyCase() {
        try {
            tryToCreateTestCourier();
        }
        finally {
            tryToCleanupTestCourier();
        }
    }

    @Step
    @Test
    public void testCreateCourierWithSameLoginWithoutPassword() {
        try {
            tryToCreateTestCourier();
            given().contentType(ContentType.JSON)
                    .body(new JSONObject()
                            .put("login", COURIER_LOGIN)
                            .put("password", "iamanewuser").toString())
                    .post("/api/v1/courier").then()
                    .assertThat()
                    .statusCode(CONFLICT_409)
                    .body("message", equalTo("Этот логин уже используется. Попробуйте другой."),
                            "code", CONFLICT_409);
        }
        finally {
            tryToCleanupTestCourier();
        }
    }

    @Step
    @Test
    public void testLoginWithBadPassword() {
        try {
            tryToCreateTestCourier();
            given().contentType(ContentType.JSON)
                    .body(new JSONObject()
                            .put("login", COURIER_LOGIN)
                            .put("password", COURIER_PASSWORD+"_boo").toString())
                    .post("/api/v1/courier/login").then()
                    .assertThat()
                    .statusCode(NOT_FOUND_404)
                    .body("message", equalTo("Учетная запись не найдена"),
                            "code", NOT_FOUND_404);
        }
        finally {
            tryToCleanupTestCourier();
        }
    }

    public static void tryToCreateTestCourier() {
        given().contentType(ContentType.JSON)
                .body(new JSONObject()
                        .put("login", COURIER_LOGIN)
                        .put("password", COURIER_PASSWORD).toString())
                .post("/api/v1/courier").then()
                .assertThat()
                .statusCode(CREATED_201)
                .body("ok", equalTo(true));
    }

    public static Integer tryToLoginTestCourier() {
        return given().contentType(ContentType.JSON)
                .body(new JSONObject()
                        .put("login", COURIER_LOGIN)
                        .put("password", COURIER_PASSWORD).toString())
                .post("/api/v1/courier/login").then()
                .assertThat()
                .statusCode(OK_200)
                .body("id", notNullValue())
                .extract().response().path("id");
    }

    public static void tryToCleanupTestCourier() {
        Integer id = tryToLoginTestCourier();
        given().contentType(ContentType.JSON)
                .delete(format("/api/v1/courier/%d", id)).then()
                .assertThat()
                .statusCode(OK_200)
                .body("ok", equalTo(true));

    }

}
