import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

public class AuthorizationCourier {

    public String login ;
    public String password ;

    Couriers couriers;

    public AuthorizationCourier(String login,String password) {
        this.login = login;
        this.password = password;
    }

    @Step("Успешная авторизация: Получение Id созданого курьера")
    public int checkNumberIdCourier() {
        var courier = new AuthorizationCourier(login,password);

        return  given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(Constant.BEARERTOKEN)
                .and().body(courier)
                .when().post(Constant.API_COURIER +"login")
                .then().log().all()
                .extract()
                .path("id");
    }

    @Step("Ошибка авторизации: Недостаточно данных для входа")
    public ValidatableResponse checkNumberIdCourierWithoutLogin() {
        var courier = new AuthorizationCourier(null,password);

        return  given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(Constant.BEARERTOKEN)
                .and().body(courier)
                .when().post(Constant.API_COURIER +"login")
                .then().log().all()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Step("Ошибка авторизации: Недостаточно данных для входа")
    public ValidatableResponse checkNumberIdCourierWithoutPassword() {
        var courier = new AuthorizationCourier(login,null);

        return  given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(Constant.BEARERTOKEN)
                .and().body(courier)
                .when().post(Constant.API_COURIER +"login")
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST);
    }

    @Step("Ошибка авторизации: неправильно указан логин")
    public ValidatableResponse checkNumberIdCourierErrorInLogin() {
        var courier = new AuthorizationCourier(login+"er",password);

        return  given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(Constant.BEARERTOKEN)
                .and().body(courier)
                .when().post(Constant.API_COURIER +"login")
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_NOT_FOUND);
    }

    @Step("Ошибка авторизации: неправильно указан пароль")
    public ValidatableResponse checkNumberIdCourierErrorInPassword() {
        var courier = new AuthorizationCourier(login,password+"1");

        return  given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(Constant.BEARERTOKEN)
                .and().body(courier)
                .when().post(Constant.API_COURIER +"login")
                .then().log().all()
                .assertThat()
                .statusCode(HTTP_NOT_FOUND);
    }
}

