import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;


public class TestCreateCourier {

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
    @DisplayName("Crete a new courier")
    @Description("Positive test is creation of a new courier")
    public void testCreateCourier() {

        ValidatableResponse response = courierClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 201", SC_CREATED, statusCode);

        boolean created = response.extract().path("ok");
        assertTrue("В ответ должно вернуться true", created);

        ValidatableResponse loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("statusCode должен быть 200", SC_OK, loginStatusCode);
        idCourier = loginResponse.extract().path("id");
        assertTrue("idCourier не должен быть равен нулю", idCourier != 0);
    }

    @Test
    @DisplayName("Create two identical couriers")
    @Description("Negative test is creating two identical couriers")
    public void testCreateTwoIdenticalCouriers() {
        courierClient.createCourier(courier);
        idCourier = courierClient.loginCourier(CourierCredentials.from(courier)).extract().path("id");

        ValidatableResponse responseDoubleCourier = courierClient.createCourier(courier);
        int statusCode = responseDoubleCourier.extract().statusCode();
        assertEquals("statusCode должен быть 409", SC_CONFLICT, statusCode);

        String created = responseDoubleCourier.extract().path("message");
        assertEquals("Ожидается: 'Этот логин уже используется'", "Этот логин уже используется", created);
    }

    @Test
    @DisplayName("Create new courier with required fields")
    @Description("Positive test is creating new courier with login and password")
    public void testCreateCourierWithRequiredFields() {
        courier.setFirstName(null);
        ValidatableResponse response = courierClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 201", SC_CREATED, statusCode);

        boolean created = response.extract().path("ok");
        assertTrue("В ответ должно вернуться true", created);

        ValidatableResponse loginCourier = courierClient.loginCourier(CourierCredentials.from(courier));
        idCourier = loginCourier.extract().path("id");
        assertTrue("idCourier не должен быть равен нулю", idCourier != 0);
    }

    @Test
    @DisplayName("Create courier without login")
    @Description("Negative test create new courier without login")
    public void testCreateCourierWithoutLogin() {
        courier.setLogin(null);
        ValidatableResponse response = courierClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 400", SC_BAD_REQUEST, statusCode);

        String created = response.extract().path("message");
        assertEquals("Ожидается: 'Недостаточно данных для создания учетной записи'",
                "Недостаточно данных для создания учетной записи", created);
    }

    @Test
    @DisplayName("Create new courier without password")
    @Description("Negative test is creating new courier without password")
    public void testCreateCourierWithoutPassword() {
        courier.setPassword(null);
        ValidatableResponse response = courierClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 400", SC_BAD_REQUEST, statusCode);

        String value = response.extract().path("message");
        assertEquals("Ожидается: 'Недостаточно данных для создания учетной записи'",
                "Недостаточно данных для создания учетной записи", value);
    }

    @Test
    @DisplayName("Create courier identical login")
    @Description("Negative test is creating two courier with identical login")
    public void testCreateTwoCourierWithIdenticalLogin() {
        courierClient.createCourier(courier);
        idCourier = courierClient.loginCourier(CourierCredentials.from(courier)).extract().path("id");

        courier.setPassword("4321");
        courier.setFirstName("Bill");
        ValidatableResponse responseCourierDoubleLogin = courierClient.createCourier(courier);
        int statusCode = responseCourierDoubleLogin.extract().statusCode();
        assertEquals("statusCode должен быть 409", SC_CONFLICT, statusCode);

        String value = responseCourierDoubleLogin.extract().path("message");
        assertEquals("Ожидается: 'Этот логин уже используется'", "Этот логин уже используется", value);
    }
}
