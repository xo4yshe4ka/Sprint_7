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

public class TestLoginCourier {

    private CourierClient courierClient;
    private CourierDelete courierDelete;
    private Courier courier;
    private int idCourier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courierDelete = new CourierDelete();
        courier = CourierGenerator.getCourier();
    }

    @After
    public void tearDown() {
        courierDelete.setId(idCourier);
        courierClient.deleteCourier(courierDelete, idCourier);
    }

    @Test
    @DisplayName("Login courier")
    @Description("Positive test is login courier")
    public void loginCourier() {
        courierClient.createCourier(courier);
        ValidatableResponse response = courierClient.loginCourier(CourierCredentials.from(courier));
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 200", SC_OK, statusCode);
        idCourier = response.extract().path("id");
        assertTrue(idCourier != 0);
    }

    @Test
    @DisplayName("Login courier with incorrect login")
    @Description("Negative test login courier with incorrect login")
    public void testLoginCourierIncorrectLogin() {
        courier.setLogin("Notrobert");
        ValidatableResponse response = courierClient.loginCourier(CourierCredentials.from(courier));
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 404", SC_NOT_FOUND, statusCode);

        String created =response.extract().path("message");
        assertEquals("Ожидается: 'Учетная запись не найдена'", "Учетная запись не найдена", created);
    }

    @Test
    @DisplayName("Login courier with incorrect password")
    @Description("Negative test login courier with incorrect password")
    public void testLoginCourierIncorrectPassword() {
        courier.setPassword("4321");
        ValidatableResponse response = courierClient.loginCourier(CourierCredentials.from(courier));
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 404", SC_NOT_FOUND, statusCode);

        String created =response.extract().path("message");
        assertEquals("Ожидается: 'Учетная запись не найдена'", "Учетная запись не найдена", created);
    }

    @Test
    @DisplayName("Login courier without login")
    @Description("Negative test login courier without login")
    public void testLoginCourierWithoutLogin() {
        courier.setLogin(null);
        ValidatableResponse response = courierClient.loginCourier(CourierCredentials.from(courier));
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 400", SC_BAD_REQUEST, statusCode);

        String created =response.extract().path("message");
        assertEquals("Ожидается: 'Недостаточно данных для входа'",
                "Недостаточно данных для входа", created);
    }

    @Test
    @DisplayName("Login courier without password")
    @Description("Negative test login courier without password")
    public void testLoginCourierWithoutPassword() {
        courier.setPassword(null);
        ValidatableResponse response = courierClient.loginCourier(CourierCredentials.from(courier));
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 400", SC_BAD_REQUEST, statusCode);

        String created =response.extract().path("message");
        assertEquals("Ожидается: 'Недостаточно данных для входа'",
                "Недостаточно данных для входа", created);
    }

    @Test
    @DisplayName("Login courier with incorrect login and password")
    @Description("Negative test login courier with incorrect login and password")
    public void testLoginCourierWithIncorrectLoginAndPassword() {
        ValidatableResponse response = courierClient.loginCourier(CourierCredentials.from(courier));
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 404", SC_NOT_FOUND, statusCode);

        String created = response.extract().path("message");
        assertEquals("Ожидается: 'Учетная запись не найдена'", "Учетная запись не найдена", created);
    }
}
