package com.yandex.spint_7;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;

public class TestOrderCourierOrderList extends BaseTest {

    @Test
    public void testGetOrdersListWithMissingData() {
        var body = new JSONObject()
                .put("login", COURIER_LOGIN)
                .put("password", COURIER_PASSWORD);
        try {
            tryCourierCreate(body);
            String id = String.valueOf((Integer) tryCourierLogin(body).then().extract().path("id"));
            var response = tryCourierGetOrdersCount(id);
            assertResponseWithCodeAndMessage(response, NOT_FOUND_404, "Not Found.");
        }
        finally {
            tryCourierDelete(body);
        }
    }

    @Step
    public Response tryCourierGetOrdersCount(String courierId) {
        return given().contentType(ContentType.JSON)
                .get(format("/api/v1/courier/%s/ordersCount", courierId));
    }
}
