package com.yandex.spint_7;

import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

public class TestCourier extends BaseTest {

    @Test
    public void testCreateCourierWithoutData() {
        var response = tryCourierCreate(null);
        assertResponseWithCodeAndMessage(response, BAD_REQUEST_400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    public void testCreateCourierWithMissingPassword() {
        var response = tryCourierCreate(new JSONObject().put("login", COURIER_LOGIN));
        assertResponseWithCodeAndMessage(response, BAD_REQUEST_400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    public void testCreateCourierWithMissingLogin() {
        var response = tryCourierCreate(new JSONObject().put("password", COURIER_PASSWORD));
        assertResponseWithCodeAndMessage(response, BAD_REQUEST_400, "Недостаточно данных для создания учетной записи");
    }

    @Ignore("api issue: gateway timeout")
    @Test
    public void testLoginWithMissingPassword() {
        var response = tryCourierLogin(new JSONObject().put("login", COURIER_LOGIN));
        assertResponseWithCodeAndMessage(response, BAD_REQUEST_400, "Недостаточно данных для входа");
    }

    @Test
    public void testLoginWithMissingLogin() {
        var response = tryCourierLogin(new JSONObject().put("password", COURIER_PASSWORD));
        assertResponseWithCodeAndMessage(response, BAD_REQUEST_400, "Недостаточно данных для входа");
    }

    @Test
    public void testLoginWithNotExistingLogin() {
        var response = tryCourierLogin(new JSONObject()
                .put("login", COURIER_LOGIN+"_boo")
                .put("password", COURIER_PASSWORD));
        assertResponseWithCodeAndMessage(response, NOT_FOUND_404, "Учетная запись не найдена");
    }

    @Test
    public void testDeleteCourierWithMissingData() {
        var response = tryCourierDelete("");
        assertResponseWithCodeAndMessage(response, NOT_FOUND_404, "Not Found.");
    }

    @Test
    public void testDeleteCourierWithBadID() {
        var code = "err";
        var response = tryCourierDelete(code);
        assertResponseWithCodeAndMessage(response, ERR_500, "invalid input syntax for type integer: \"" + code + "\"");
    }

    @Test
    public void testDeleteCourierWithNotExistingID() {
        var response = tryCourierDelete("0");
        assertResponseWithCodeAndMessage(response, NOT_FOUND_404, "Курьера с таким id нет.");
    }

    @Test
    public void testCreateCourierHappyCase() {
        var body = new JSONObject()
                .put("login", COURIER_LOGIN)
                .put("password", COURIER_PASSWORD);
        try {
            var response = tryCourierCreate(body);
            assertResponseCreatedOk(response);
        }
        finally {
            tryCourierDelete(body);
        }
    }

    @Test
    public void testCreateCourierWithSameLoginWithoutPassword() {
        var body = new JSONObject()
                .put("login", COURIER_LOGIN)
                .put("password", COURIER_PASSWORD);
        try {
            tryCourierCreate(body);
            var response = tryCourierCreate(body);
            assertResponseWithCodeAndMessage(response, CONFLICT_409, "Этот логин уже используется. Попробуйте другой.");
        }
        finally {
            tryCourierDelete(body);
        }
    }

    @Test
    public void testLoginWithBadPassword() {
        var body = new JSONObject()
                .put("login", COURIER_LOGIN)
                .put("password", COURIER_PASSWORD);
        try {
            tryCourierCreate(body);
            var response = tryCourierLogin(new JSONObject()
                    .put("login", COURIER_LOGIN)
                    .put("password", COURIER_PASSWORD+"_boo"));
            assertResponseWithCodeAndMessage(response, NOT_FOUND_404, "Учетная запись не найдена");
        }
        finally {
            tryCourierDelete(body);
        }
    }

}
