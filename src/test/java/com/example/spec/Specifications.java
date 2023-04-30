package com.example.spec;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import static com.example.helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.http.ContentType.JSON;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.lessThanOrEqualTo;


public class Specifications {
    protected static final String apiKey = "c7c34b8368ee78c0746bb305b6580a68";

    public static RequestSpecification requestSpec(String url) {

        return new RequestSpecBuilder()
                .setBaseUri(url)
                .addQueryParam("appid", apiKey)
                .log(LogDetail.ALL)//---> Уровень логирования
                .setContentType(JSON)//---> Установка Content Type
                .setAccept(JSON)//---> Установка Accept
                .addFilter(withCustomTemplates()) //---> Добавление красивого шаблона для отчетов
                .build();
    }


    public static ResponseSpecification responseSpecOK200() {
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)//---> Уровень логирования
                .expectContentType(JSON)//---> Ожидаемый Content Type
                .expectStatusCode(HttpStatus.SC_OK)//---> Ожидаемый Status Code
                .expectResponseTime(lessThanOrEqualTo(3L), SECONDS)//---> Ожидаемое время ответа максимум 3 секунды
                .build();
    }

    public static ResponseSpecification responseSpecBadRequest400() {
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)//---> Уровень логирования
                .expectContentType(JSON)//---> Ожидаемый Content Type
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)//---> Ожидаемый Status Code
                .expectResponseTime(lessThanOrEqualTo(3L), SECONDS)//---> Ожидаемое время ответа максимум 3 секунды
                .build();
    }

    public static ResponseSpecification responseSpecNotFound404() {
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)//---> Уровень логирования
                .expectContentType(JSON)//---> Ожидаемый Content Type
                .expectStatusCode(HttpStatus.SC_NOT_FOUND)//---> Ожидаемый Status Code
                .expectResponseTime(lessThanOrEqualTo(3L), SECONDS)//---> Ожидаемое время ответа максимум 3 секунды
                .build();
    }


    public static void installRequestSpecification(RequestSpecification requestSpec) {
        RestAssured.requestSpecification = requestSpec;
    }

    public static void installResponseSpecification(ResponseSpecification responseSpec) {
        RestAssured.responseSpecification = responseSpec;
    }


}
