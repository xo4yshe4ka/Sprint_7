import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TestGetListOrders {

    private OrderClient orderClient;
    private OrderCancel orderCancel;

    private Order order;
    private int track;


    @Before
    public void setUp() {
        orderClient = new OrderClient();
        orderCancel = new OrderCancel();
        order = OrderGenerator.getOrder();
    }

    @After
    public void tearDown() {
        orderCancel.setTrack(track);
        orderClient.cancelOrder(orderCancel);
    }

    @Test
    @DisplayName("Get list all orders")
    public void testGetListOrders() {
        track = orderClient.createOrder(order).extract().path("track");

        ValidatableResponse response = orderClient.getListOrders();
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 200", SC_OK, statusCode);

        String contentType = response.extract().contentType();
        assertEquals("application/json; charset=utf-8", contentType);

        ArrayList<String> orders = response.extract().path("orders");
        assertThat("Список заказов не должен быть пустым", orders, is(not(empty())));
    }
}
