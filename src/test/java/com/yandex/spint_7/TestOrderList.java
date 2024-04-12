package com.yandex.spint_7;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.iterableWithSize;

public class TestOrderList extends BaseTest {
    @Test
    public void testOrderList() {
        int limit = 10;
        var response = tryOrdersListFetch(limit);
        assertThatOrdersListedWithLimit(response, limit);
    }

    @Step
    public Response tryOrdersListFetch(Integer limit) {
        return given().contentType(ContentType.JSON)
                .queryParam("limit", limit)
                .get("/api/v1/orders");
    }

    @Step
    public void assertThatOrdersListedWithLimit(Response response, Integer limit) {
        response.then()
                .assertThat()
                .statusCode(OK_200)
                .body("orders", iterableWithSize(limit));
    }
}
