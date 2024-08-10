import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.OrderCancel;
import org.example.Order;
import org.example.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(Parameterized.class)
public class TestCreateOrder {

    private OrderClient orderClient;
    private OrderCancel orderCancel;
    private Order order;
    private int track;

    public TestCreateOrder(Order order) {
        this.order = order;
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        orderCancel = new OrderCancel();
    }

    @After
    public void tearDown() {
        orderCancel.setTrack(track);
        orderClient.cancelOrder(orderCancel);
    }

    @Parameterized.Parameters
    public static Object[][] getDataOrders() {
        return new Object[][]{
                {new Order("Иван", "Иванов", "Москва", "4",
                        "+7 800 355 35 35", "5", "2020-06-06", "Comment",
                        new String[]{"BLACK"})},
                {new Order("Иван", "Иванов", "Москва", "4",
                        "+7 800 355 35 35", "5", "2020-06-06", "Comment",
                        new String[]{"GREY"})},
                {new Order("Иван", "Иванов", "Москва", "4",
                        "+7 800 355 35 35", "5", "2020-06-06", "Comment",
                        new String[]{"BLACK", "GREY"})},
                {new Order("Иван", "Иванов", "Москва", "4",
                        "+7 800 355 35 35", "5", "2020-06-06", "Comment",
                        new String[]{})}
        };
    }

    @Test
    @DisplayName("Crete a new order")
    @Description("Positive test is creation of a new order")
    public void testCreateOrder() {
        ValidatableResponse response = orderClient.createOrder(order);
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 201", SC_CREATED, statusCode);

        track = response.extract().path("track");
        assertNotEquals("created не должен быть равен нулю", 0, track);
    }
}
