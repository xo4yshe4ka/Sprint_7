package org.example;

public class OrderGenerator {
    public static Order getOrder() {
        Order order = new Order();
        order.setFirstName("Иван");
        order.setLastName("Иванов");
        order.setAddress("Москва");
        order.setMetroStation("2");
        order.setPhone("+7 800 355 35 35");
        order.setRentTime("2");
        order.setDeliveryDate("2024-08-08");
        order.setComment("Коммент");
        order.setColor(new String[]{"GREY"});
        return order;
    }
}
