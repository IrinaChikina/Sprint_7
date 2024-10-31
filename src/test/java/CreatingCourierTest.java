import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
public class CreatingCourierTest {

    String login = RandomStringUtils.random(6,true,true);
    String password = RandomStringUtils.random(4,true,true);
    String firstName = RandomStringUtils.random(10,true,false);

    int courierId;

    Couriers couriers= new Couriers(login, password, firstName);
    Couriers couriersWithoutPassword = new Couriers(login, null, firstName);
    Couriers couriersWithoutLogin = new Couriers(null, password, firstName);
    Couriers couriersWithoutFirstName = new Couriers(login, password);

    @Step ("Запуск Яндекс.Самокат")
    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.URLMETRO;
    }

    @Test
    @DisplayName("Check creating Courier")
    @Description ("POST /api/v1/courier")
    public void createdCourierTest() {
        Response response = couriers.sendPostCreatingCourier(couriers);
        couriers.checkCreatedOK(response); // Проверка статуса кода

        var couriersCreated = new AuthorizationCourier(login,password);
        courierId =  couriersCreated.checkNumberIdCourier(); // Получение Id курьера
    }


    @Test
    @DisplayName("Сannot create two identical couriers")
    @Description ("POST /api/v1/courier")
    public void createdEqualCourierTest() {
        Response responseOK = couriers.sendPostCreatingCourier(couriers);
        couriers.checkCreatedOK(responseOK);// Проверка статуса кода

        ValidatableResponse create = couriers.checkCreatedСonflict(couriers);
        Assert.assertEquals("Текст сообщения об ошибки:" + create, "Этот логин уже используется", create.extract().path("message"));

        var couriersCreated = new AuthorizationCourier(login,password);
        courierId =  couriersCreated.checkNumberIdCourier(); // Получение Id курьера
    }

    @Test
    @DisplayName("Check creating Courier only with required fields")
    @Description ("POST /api/v1/courier")
    public void createdCourierWithoutFirstNameTest() {
        Response response = couriers.sendPostCreatingCourier(couriersWithoutFirstName);
        couriers.checkCreatedOK(response); // Проверка статуса кода

        var couriersCreated = new AuthorizationCourier (login, password);
        courierId = couriersCreated.checkNumberIdCourier(); // Получение Id курьера
    }
    @Test
    @DisplayName("Check creating Courier without password")
    @Description ("POST /api/v1/courier")
    public void createdCourierWithoutPasswordTest() {
        ValidatableResponse create =  couriers.checkCreatedBedRequest(couriersWithoutPassword); // Проверка статуса кода
        Assert.assertEquals("Текст сообщения об ошибки:" + create, "Недостаточно данных для создания учетной записи", create.extract().path("message"));
    }

    @Test
    @DisplayName("Check creating Courier without login")
    @Description ("POST /api/v1/courier")
    public void createdCourierWithoutLoginTest() {
        ValidatableResponse create =  couriers.checkCreatedBedRequest(couriersWithoutLogin); // Проверка статуса кода
        Assert.assertEquals("Текст сообщения об ошибки:" + create, "Недостаточно данных для создания учетной записи", create.extract().path("message"));
    }



    @After
    public void deleteCourier() {
        if (courierId != 0)
            couriers.deleteCourier(courierId);
    }
}




