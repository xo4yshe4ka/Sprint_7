package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.example.BaseApi.*;

public class CourierClient extends RestClient {

    @Step
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                        .spec(getBaseSpec())
                        .body(courier)
                        .when()
                        .post(POST_CREATE_COURIER)
                        .then();
    }

    @Step
    public ValidatableResponse loginCourier(CourierCredentials credentials) {
        return given()
                        .spec(getBaseSpec())
                        .body(credentials)
                        .when()
                        .post(POST_LOGIN_COURIER)
                        .then();
    }

    @Step
    public ValidatableResponse deleteCourier(CourierDelete courierDelete, int idCourier) {
        return given()
                        .spec(getBaseSpec())
                        .body(courierDelete)
                        .when()
                        .delete(DELETE_COURIER + idCourier)
                        .then();
    }

    @Step
    public ValidatableResponse deleteCourier() {
        return given()
                .spec(getBaseSpec())
                .when()
                .delete(DELETE_COURIER)
                .then();
    }
}
