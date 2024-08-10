import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.OrderCancel;
import org.example.Order;
import org.example.OrderClient;
import org.example.OrderGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class TestGetOrderByNumber {

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
    @DisplayName("Get order by number order")
    public void testGetOrderByNumber() {
        track = orderClient.createOrder(order).extract().path("track");
        ValidatableResponse response = orderClient.getOrderByNumber(track);

        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 200", SC_OK, statusCode);

        String contentType = response.extract().contentType();
        assertEquals("application/json; charset=utf-8", contentType);

        Map<String, Object> orderMap = response.extract().path("order");
        assertNotNull(orderMap);
    }

    @Test
    @DisplayName("Get order by incorrect number number")
    public void testGetOrderWithIncorrectNumberOrder() {
        track = 98547;
        ValidatableResponse response = orderClient.getOrderByNumber(track);

        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 404", SC_NOT_FOUND, statusCode);

        String created = response.extract().path("message");
        assertEquals("Ожидается: 'Заказ не найден'", "Заказ не найден", created);
    }

    @Test
    @DisplayName("Get order without number number")
    public void testGetOrderWithoutNumberOrder() {
        ValidatableResponse response = orderClient.getOrderByNumber();

        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 400", SC_BAD_REQUEST, statusCode);

        String created = response.extract().path("message");
        assertEquals("Ожидается: 'Недостаточно данных для поиска'",
                "Недостаточно данных для поиска", created);
    }
}
