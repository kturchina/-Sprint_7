package com.yandex.spint_7;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static com.yandex.spint_7.TestCourier.tryToCleanupTestCourier;
import static com.yandex.spint_7.TestCourier.tryToCreateTestCourier;
import static com.yandex.spint_7.TestCourier.tryToLoginTestCourier;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

@Ignore("Endpoint is broken")
public class TestOrderCourierOrderList extends BaseTest {

    @Step
    @Test
    public void testGetOrdersListWithMissingData() {
        try {
            tryToCreateTestCourier();
            Integer id = tryToLoginTestCourier();
            given().contentType(ContentType.JSON)
                    .get(format("/api/v1/courier/%d/ordersCount", id)).then()
                    .assertThat()
                    .statusCode(NOT_FOUND_404)
                    .body("message", equalTo("Not Found."),
                            "code", NOT_FOUND_404);
        }
        finally {
            tryToCleanupTestCourier();
        }
    }
}
