package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.example.BaseApi.*;

public class OrderClient extends RestClient {

    @Step
    public ValidatableResponse createOrder(Order order) {
        return given()
                    .spec(getBaseSpec())
                    .body(order)
                    .when()
                    .post(POST_CREATE_ORDERS)
                    .then();
    }

    @Step
    public ValidatableResponse cancelOrder(OrderCancel cancelOrder) {
        return given()
                    .spec(getBaseSpec())
                    .body(cancelOrder)
                    .when()
                    .put(PUT_CANCEL_ORDER)
                    .then();
    }

    @Step
    public ValidatableResponse getListOrders() {
        return given()
                    .spec(getBaseSpec())
                    .when()
                    .get(GET_LIST_ORDERS)
                    .then();
    }

    @Step
    public ValidatableResponse getOrderByNumber(int track) {
        return given()
                    .spec(getBaseSpec())
                    .queryParam("t", track)
                    .when()
                    .get(GET_ORDER_BY_NUMBER)
                    .then();
    }

    @Step
    public ValidatableResponse getOrderByNumber() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(GET_ORDER_BY_NUMBER)
                .then();
    }

    @Step
    public ValidatableResponse acceptOrder(int idOrder, int idCourier) {
        return given()
                        .spec(getBaseSpec())
                        .pathParam("id", idOrder)
                        .queryParam("courierId", idCourier)
                .log().all()
                        .when()
                        .put(PUT_ACCEPT_ORDER)
                .then();
    }

    @Step
    public ValidatableResponse acceptOrderWithoutIdOrder(int idCourier) {
        return given()
                .spec(getBaseSpec())
                .queryParam("courierId", idCourier)
                .log().all()
                .when()
                .put(PUT_ACCEPT_ORDER_WITHOUT_ID_ORDER)
                .then();
    }

    @Step
    public ValidatableResponse acceptOrder(int idOrder) {
        return given()
                .spec(getBaseSpec())
                .pathParam("id", idOrder)
                .when()
                .put(PUT_ACCEPT_ORDER)
                .then();
    }

}
