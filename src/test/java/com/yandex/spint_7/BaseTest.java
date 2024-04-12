package com.yandex.spint_7;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class BaseTest {
    public static final String COURIER_LOGIN = "atest01042024";
    public static final String COURIER_PASSWORD = "ptest01042024";

    public static final Matcher<Integer> BAD_REQUEST_400 = equalTo(400);
    public static final Matcher<Integer> NOT_FOUND_404 = equalTo(404);
    public static final Matcher<Integer> CONFLICT_409 = equalTo(409);

    public static final Matcher<Integer> CREATED_201 = equalTo(201);
    public static final Matcher<Integer> OK_200 = equalTo(200);

    public static final Matcher<Integer> ERR_500 = equalTo(500);

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        RestAssured.port = 80;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Step
    public Response tryCourierCreate(JSONObject body) {
        var rq = given().contentType(ContentType.JSON);
        if (body != null) {
            rq = rq.body(body.toString());
        }
        return rq.post("/api/v1/courier");
    }

    @Step
    public Response tryCourierLogin(JSONObject body) {
        var rq = given().contentType(ContentType.JSON);
        if (body != null) {
            rq = rq.body(body.toString());
        }
        return rq.post("/api/v1/courier/login");
    }

    @Step
    public Response tryCourierDelete(String numericId) {
        return given().contentType(ContentType.JSON)
                .delete(format("/api/v1/courier/%s", numericId));
    }

    @Step
    public void tryCourierDelete(JSONObject body) {
        var response = tryCourierLogin(body);
        Integer id = response.then().assertThat()
                .statusCode(OK_200)
                .body("id", notNullValue())
                .extract().response().path("id");
        tryCourierDelete(String.valueOf(id));
    }

    @Step
    public void assertResponseWithCodeAndMessage(Response response, Matcher<Integer> code, String message) {
        response.then().assertThat().statusCode(code)
                .body("message", equalTo(message),
                        "code", code);
    }

    @Step
    public void assertResponseCreatedOk(Response response) {
        response.then().assertThat().statusCode(CREATED_201)
                .body("ok", equalTo(true));
    }
}
