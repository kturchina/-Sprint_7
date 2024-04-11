package com.yandex.spint_7;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;

import static org.hamcrest.Matchers.equalTo;

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
}
