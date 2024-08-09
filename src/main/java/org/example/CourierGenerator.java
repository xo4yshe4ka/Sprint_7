package org.example;

public class CourierGenerator {

    public static Courier getCourier() {
        Courier courier = new Courier();
        courier.setLogin("Robertp");
        courier.setPassword("1234");
        courier.setFirstName("Bob");

        return courier;
    }

}
