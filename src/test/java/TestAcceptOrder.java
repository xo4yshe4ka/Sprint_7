import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestAcceptOrder {

    private OrderClient orderClient;
    private Order order;

    private CourierClient courierClient;
    private CourierDelete courierDelete;
    private Courier courier;

    private int idCourier;
    private int idOrder;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        courierClient = new CourierClient();
        courierDelete = new CourierDelete();
        order = OrderGenerator.getOrder();
        courier = CourierGenerator.getCourier();
    }

    @After
    public void tearDown() {
        courierDelete.setId(idCourier);
        courierClient.deleteCourier(courierDelete, idCourier);
    }


    @Test
    @DisplayName("Accept order")
    public void testAcceptOrder() {
        int track = orderClient.createOrder(order).extract().path("track");
        idOrder = orderClient.getOrderByNumber(track).extract().path("order.id");

        courierClient.createCourier(courier);
        idCourier = courierClient.loginCourier(CourierCredentials.from(courier)).extract().path("id");

        ValidatableResponse response = orderClient.acceptOrder(idOrder, idCourier);
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 200", SC_OK, statusCode);

        boolean created = response.extract().path("ok");
        assertTrue(created);
    }

    @Test
    @DisplayName("Accept order with incorrect id order")
    public void testAcceptOrderWithIncorrectIdOrder() {
        idOrder = 999999;
        courierClient.createCourier(courier);
        idCourier = courierClient.loginCourier(CourierCredentials.from(courier)).extract().path("id");

        ValidatableResponse response = orderClient.acceptOrder(idOrder, idCourier);
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 404", SC_NOT_FOUND, statusCode);

        String created = response.extract().path("message");
        assertEquals("Ожидается: 'Заказа с таким id не существует'",
                "Заказа с таким id не существует", created);
    }

    @Test
    @DisplayName("Accept order with incorrect id courier")
    public void testAcceptOrderIncorrectCourierId() {
        idCourier = 999999;
        int track = orderClient.createOrder(order).extract().path("track");
        idOrder = orderClient.getOrderByNumber(track).extract().path("order.id");

        ValidatableResponse response = orderClient.acceptOrder(idOrder, idCourier);
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 404", SC_NOT_FOUND, statusCode);

        String created = response.extract().path("message");
        assertEquals("Ожидается: 'Курьера с таким id не существует'",
                "Курьера с таким id не существует", created);
    }

    @Test
    @DisplayName("Accept order without id order")
    public void testAcceptOrderWithoutIdOrder() {
        courierClient.createCourier(courier);
        idCourier = courierClient.loginCourier(CourierCredentials.from(courier)).extract().path("id");
        System.out.println(idCourier);
        ValidatableResponse response = orderClient.acceptOrderWithoutIdOrder(idCourier);
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 400", SC_BAD_REQUEST, statusCode);

        String created = response.extract().path("message");
        assertEquals("Ожидается: 'Недостаточно данных для поиска'",
                "Недостаточно данных для поиска", created);
    }

    @Test
    @DisplayName("Accept order without id courier")
    public void testAcceptOrderWithoutCourierId() {
        int track = orderClient.createOrder(order).extract().path("track");
        idOrder = orderClient.getOrderByNumber(track).extract().path("order.id");

        ValidatableResponse response = orderClient.acceptOrder(idOrder);
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 400", SC_BAD_REQUEST, statusCode);

        String created = response.extract().path("message");
        assertEquals("Ожидается: 'Недостаточно данных для поиска'",
                "Недостаточно данных для поиска", created);
    }
}
