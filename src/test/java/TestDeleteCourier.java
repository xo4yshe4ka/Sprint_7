import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.*;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestDeleteCourier {
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

    @Test
    @DisplayName("Delete courier")
    public void testDeleteCourier() {
        courierClient.createCourier(courier);
        idCourier = courierClient.loginCourier(CourierCredentials.from(courier)).extract().path("id");
        courierDelete.setId(idCourier);

        ValidatableResponse response = courierClient.deleteCourier(courierDelete, idCourier);

        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 200", SC_OK, statusCode);

        boolean created = response.extract().path("ok");
        assertTrue(created);
    }

    @Test
    @DisplayName("Delete courier with incorrect id")
    public void testDeleteCourierWithIncorrectId() {
        idCourier = 123525;

        ValidatableResponse response = courierClient.deleteCourier(courierDelete, idCourier);

        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 404", SC_NOT_FOUND, statusCode);

        String created = response.extract().path("message");
        assertEquals("Ожидается: 'Курьера с таким id нет'",
                "Курьера с таким id нет", created);
    }

    @Test
    @DisplayName("Delete courier without id courier")
    public void testDeleteCourierWithoutId() {

        ValidatableResponse response = courierClient.deleteCourier();

        int statusCode = response.extract().statusCode();
        assertEquals("statusCode должен быть 400", SC_BAD_REQUEST, statusCode);

        String created = response.extract().path("message");
        assertEquals("Ожидается: 'Недостаточно данных для удаления курьера'",
                "Недостаточно данных для удаления курьера", created);
    }

}