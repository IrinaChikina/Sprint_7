import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;

public class Couriers {
    public String login;
    public String password;
    public String firstName;


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public Couriers(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public Couriers(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Step("Запрос на создание нового курьера")
    public Response sendPostCreatingCourier(Couriers couriers) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(Constant.BEARERTOKEN)
                .body(couriers)
                .when().post(Constant.API_COURIER);
    }

    @Step("Курьер успешно создан")
    public void checkCreatedOK(Response response) {
        boolean create = response
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_CREATED)
                .extract().path("ok");
        Assert.assertTrue(create);
    }

    @Step("Курьер не создан")
    public ValidatableResponse checkCreatedСonflict(Couriers couriers) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(Constant.BEARERTOKEN)
                .body(couriers)
                .when().post(Constant.API_COURIER)
                .then().log().all()
                .statusCode(HTTP_CONFLICT);
    }

    @Step("Курьер не создан: Недостаточно данных для создания учетной записи")
    public ValidatableResponse checkCreatedBedRequest (Couriers couriers) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(Constant.BEARERTOKEN)
                .body(couriers)
                .when().post(Constant.API_COURIER)
                .then().log().all()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Step ("Удаление курьера")
    public void deleteCourier (int courierId){
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(Constant.BEARERTOKEN)
                .when().delete(Constant.API_COURIER + courierId)
                .then().log().all()
                .assertThat().statusCode(HTTP_OK);
    }
}

