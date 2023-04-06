package com.example.tests;

import com.example.TestBase;
import com.example.models.ErrorResponse;
import com.example.models.WeatherResponse;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.example.spec.Specifications.*;
import static com.example.spec.Specifications.responseSpecOK200;
import static io.restassured.RestAssured.given;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@DisplayName("Check weather")
public class WeatherApiTests extends TestBase {

    // Тестовые данные
    protected final String validQuery = "London";
    protected final String validId = "2172797";
    protected final String validLat = "35";
    protected final String validLon = "139";
    protected final String validZip = "95050";
    protected final String validUnits = "standart";
    protected final String validLang = "en";
    protected final String validMode = "json";
    protected final String invalidQuery = "sdfsdf";


    @BeforeAll
    public static void setUp() {
        installRequestSpecification(requestSpec(URL));
    }

    @Test
    @Feature("Weather")
    @Story("Weather")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Check Nothing to geocode")
    void weatherErrorTest() {
        installResponseSpecification(responseSpecBadRequest400());

        ErrorResponse weatherResponse = given()
                .when()
                .get("weather")
                .then()
                .extract().as(ErrorResponse.class);

        assertThat(weatherResponse.getCod()).isEqualTo("400");
        assertThat(weatherResponse.getMessage()).isEqualTo("Nothing to geocode");
    }

    @Test
    @Feature("Weather")
    @Story("Weather")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Check valid query")
    void weatherValidTest() {
        installResponseSpecification(responseSpecOK200());
        // Создаем query запрос
        Map<String, String> data = new HashMap<>();
        data.put("q", validQuery);
        data.put("id", validId);
        data.put("lat", validLat);
        data.put("lon", validLon);
        data.put("zip", validZip);
        data.put("units", validUnits);
        data.put("lang", validLang);
        data.put("mode", validMode);

        WeatherResponse weatherResponse = given()
                .queryParams(data)
                .when()
                .get("weather")
                .then()
                .extract().as(WeatherResponse.class);

        assertThat(weatherResponse.getWeather().get(0).getId()).isEqualTo(801);
        assertThat(weatherResponse.getId()).isEqualTo(2643743);
        assertThat(weatherResponse.getName()).isEqualTo(validQuery);
        assertThat(weatherResponse.getCoord().getLon()).isEqualTo(-0.1257);
    }


    @Test
    @Feature("Weather")
    @Story("Weather")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check city not found")
    void weatherCityNotFoundTest() {
        installResponseSpecification(responseSpecNotFound404());
        // Создаем query запрос
        Map<String, String> data = new HashMap<>();
        data.put("q", invalidQuery);
        data.put("id", validId);
        data.put("lat", validLat);
        data.put("lon", validLon);
        data.put("zip", validZip);
        data.put("units", validUnits);
        data.put("lang", validLang);
        data.put("mode", validMode);

        ErrorResponse weatherResponse = given()
                .queryParams(data)
                .when()
                .get("weather")
                .then()
                .extract().as(ErrorResponse.class);

        assertThat(weatherResponse.getCod()).isEqualTo("404");
        assertThat(weatherResponse.getMessage()).isEqualTo("city not found");
    }


    @Test
    @Feature("Weather")
    @Story("Weather")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Check weather without ID")
    void weatherWithoutIdTest() {
        installResponseSpecification(responseSpecOK200());
        // Создаем query запрос
        Map<String, String> data = new HashMap<>();
        data.put("q", validQuery);
        data.put("lat", validLat);
        data.put("lon", validLon);
        data.put("zip", validZip);
        data.put("units", validUnits);
        data.put("lang", validLang);
        data.put("mode", validMode);

        WeatherResponse weatherResponse = given()
                .queryParams(data)
                .when()
                .get("weather")
                .then()
                .extract().as(WeatherResponse.class);

        assertThat(weatherResponse.getWeather().get(0).getId()).isEqualTo(801);
        assertThat(weatherResponse.getName()).isEqualTo(validQuery);
        assertThat(weatherResponse.getCoord().getLon()).isEqualTo(-0.1257);
    }

    @Test
    @Feature("Weather")
    @Story("Weather")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Check error city ID")
    void weatherCityIdNotFoundTest() {
        installResponseSpecification(responseSpecBadRequest400());
        // Создаем query запрос
        Map<String, String> data = new HashMap<>();

        data.put("id", invalidQuery);


        ErrorResponse weatherResponse = given()
                .queryParams(data)
                .when()
                .get("weather")
                .then()
                .extract().as(ErrorResponse.class);

        assertThat(weatherResponse.getCod()).isEqualTo("400");
        assertThat(weatherResponse.getMessage()).isEqualTo(invalidQuery + " is not a city ID");
    }

    @Test
    @Feature("Weather")
    @Story("Weather")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check invalid ZIP query")
    void weatherInvalidZipTest() {
        installResponseSpecification(responseSpecNotFound404());
        // Создаем query запрос
        Map<String, String> data = new HashMap<>();

        data.put("zip", invalidQuery);

        ErrorResponse weatherResponse = given()
                .queryParams(data)
                .when()
                .get("weather")
                .then()
                .extract().as(ErrorResponse.class);

        assertThat(weatherResponse.getCod()).isEqualTo("404");
        assertThat(weatherResponse.getMessage()).isEqualTo("city not found");
    }


    @Test
    @Feature("Weather")
    @Story("Weather")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Check error JSON")
    void invalidJsonTest() {
        installResponseSpecification(responseSpecBadRequest400());

        given()
                .when()
                .get("weather")
                .then()
                .body(matchesJsonSchemaInClasspath("schema/weather/errorResponse.json"));


    }

    @Test
    @Feature("Weather")
    @Story("Weather")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Check valid JSON")
    void validJsonTest() {
        installResponseSpecification(responseSpecOK200());
        // Создаем query запрос
        Map<String, String> data = new HashMap<>();
        data.put("q", validQuery);
        data.put("id", validId);
        data.put("lat", validLat);
        data.put("lon", validLon);
        data.put("zip", validZip);
        data.put("units", validUnits);
        data.put("lang", validLang);
        data.put("mode", validMode);

        given()
                .queryParams(data)
                .when()
                .get("weather")
                .then()
                .body(matchesJsonSchemaInClasspath("schema/weather/validResponse.json"));

    }


}
