package org.example;

public class BaseApi {
    static final String Y_SCOOTER_URL = "https://qa-scooter.praktikum-services.ru";
    static final String POST_CREATE_COURIER = "/api/v1/courier";
    static final String POST_LOGIN_COURIER = "/api/v1/courier/login";
    static final String POST_CREATE_ORDERS = "/api/v1/orders";
    static final String GET_LIST_ORDERS = "/api/v1/orders";
    static final String DELETE_COURIER = "/api/v1/courier/";
    static final String PUT_ACCEPT_ORDER = "/api/v1/orders/accept/{id}";
    static final String PUT_ACCEPT_ORDER_WITHOUT_ID_ORDER = "/api/v1/orders/accept/";
    static final String GET_ORDER_BY_NUMBER = "/api/v1/orders/track";
    static final String PUT_CANCEL_ORDER = "/api/v1/orders/cancel";
}
