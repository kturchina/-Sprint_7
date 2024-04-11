package com.yandex.spint_7;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.iterableWithSize;

public class TestOrderList extends BaseTest {
    @Step
    @Test
    public void testOrderList() {
        int limit = 10;
        given().contentType(ContentType.JSON)
                .queryParam("limit", limit)
                .get("/api/v1/orders")
                .then()
                .assertThat()
                .statusCode(OK_200)
                .body("orders", iterableWithSize(limit));
    }
}
