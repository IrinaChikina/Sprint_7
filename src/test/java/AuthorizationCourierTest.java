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

public class AuthorizationCourierTest {


    String login = RandomStringUtils.random(6,true,true);
    String password = RandomStringUtils.random(4,true,true);

    int courierId;

    Couriers couriers= new Couriers(login, password);

    @Step("Запуск Яндекс.Самокат")
    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.URLMETRO;
    }

    @Test
    @DisplayName("Check Authorization Courier without login")
    @Description("POST /api/v1/courier/login")
    public void authorizationCourierWithoutLogin() {

        Response response = couriers.sendPostCreatingCourier(couriers);
        couriers.checkCreatedOK(response); // Регистрация курьера

        var courier = new AuthorizationCourier(null,password);
        ValidatableResponse result = courier.checkNumberIdCourierWithoutLogin();
        Assert.assertEquals("Недостаточно данных для входа", result.extract().path("message"));
    }


    @Test
    @DisplayName("Check Authorization Courier without password")
    @Description("POST /api/v1/courier/login")
    public void authorizationCourierWithoutPassword() {

        Response response = couriers.sendPostCreatingCourier(couriers);
        couriers.checkCreatedOK(response); // Регистрация курьера

        var courier = new AuthorizationCourier(login, null);
        ValidatableResponse result = courier.checkNumberIdCourierWithoutPassword();
        Assert.assertEquals("Недостаточно данных для входа", result.extract().path("message"));
    }

    @Test
    @DisplayName("Check Authorization with error in courier's login")
    @Description("POST /api/v1/courier/login")
    public void authorizationCourierWithErrorInLogin() {

        Response response = couriers.sendPostCreatingCourier(couriers);
        couriers.checkCreatedOK(response); // Регистрация курьера

        var courier = new AuthorizationCourier(login+"er",password);
        ValidatableResponse result = courier.checkNumberIdCourierErrorInLogin();
        Assert.assertEquals("Учетная запись не найдена", result.extract().path("message"));
    }

    @Test
    @DisplayName("Check Authorization with error in courier's password")
    @Description("POST /api/v1/courier/login")
    public void authorizationCourierWithErrorInPassword() {

        Response response = couriers.sendPostCreatingCourier(couriers);
        couriers.checkCreatedOK(response); // Регистрация курьера

        var courier = new AuthorizationCourier(login, password+"1");
        ValidatableResponse result = courier.checkNumberIdCourierErrorInPassword();
        Assert.assertEquals("Учетная запись не найдена", result.extract().path("message"));
    }

    @After
    @Step ("Удаление курьера")
    public void deleteCourier() {
        if (courierId != 0)
            couriers.deleteCourier(courierId);
    }
}
