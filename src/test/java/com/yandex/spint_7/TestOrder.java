package com.yandex.spint_7;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class TestOrder extends BaseTest {

    private final List<String> colors;

    public TestOrder(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Collection<Object> data() {
        return Arrays.asList(new Object[] {
            List.of(""),
            List.of("BLACK"),
            List.of("GREY"),
            List.of("BLACK", "GREY")
        });
    }

    @Test
    public void testOrderCreate() {
        var response = tryOrderCreate(new JSONObject()
                .put("firstName", "Naruto")
                .put("lastName", "Uchiha")
                .put("address", "Konoha, 142 apt.")
                .put("metroStation", 4)
                .put("phone", "+7 800 355 35 35")
                .put("rentTime", 5)
                .put("deliveryDate", "2020-06-06")
                .put("comment", "Saske, come back to Konoha")
                .put("color", colors));
        assertOrderCreatedWithTrack(response);
        Integer track = response.then().extract().path("track");
        /* не работает
        tryOrderCancel(track);
         */
    }

    @Step
    public Response tryOrderCreate(JSONObject body) {
        return given().contentType(ContentType.JSON)
                .body(body.toString())
                .post("/api/v1/orders");
    }

    @Step
    public void assertOrderCreatedWithTrack(Response response) {
        response.then()
                .assertThat()
                .statusCode(CREATED_201)
                .body("track", notNullValue());
    }

    @Step
    public void tryOrderCancel(Integer trackId) {
        given().contentType(ContentType.JSON)
                .body(new JSONObject().put("track", trackId).toString())
                .put("/api/v1/orders/cancel").then()
                .assertThat()
                .statusCode(OK_200)
                .body("ok", equalTo(true));
    }

}
