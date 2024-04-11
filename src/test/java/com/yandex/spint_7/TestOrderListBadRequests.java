package com.yandex.spint_7;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
@Ignore("Endpoint is broken")
public class TestOrderListBadRequests extends BaseTest {
    @Parameterized.Parameters
    public static Collection<Object> data() {
        return Arrays.asList(new Object[] {
                "",
                "asdad",
                "0"
        });
    }

    private final String id;
    public TestOrderListBadRequests(String id) {
        this.id = id;
    }

    @Step
    @Test
    public void testGetOrdersListWithMissingData() {
        fail("Endpoint is broken");
        given().contentType(ContentType.JSON)
                .get(format("/api/v1/courier/%s/ordersCount", id)).then()
                .assertThat()
                .statusCode(NOT_FOUND_404)
                .body("message", equalTo("Not Found."),
                        "code", NOT_FOUND_404);
    }
}
