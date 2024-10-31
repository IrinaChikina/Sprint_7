import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderCreated {

    @Step("Создание заказа")
    public Response CreatedOrder(OrderCreated orderCreated) {
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(Constant.BEARERTOKEN)
                .and().body(orderCreated)
                .when()
                .post(Constant.API_ORDERS);
        return response;
    }

    @Step("Заказ создан: проверка статуса")
    public void checkStatusCodeCreatedOrder(Response response) {
        response.then()
                .assertThat().statusCode(HTTP_CREATED); // проверка статуса
    }

    @Step("Ответ с трек номером поступил")
    public void checkTrackCreatedOrder(Response response) {
        response.then()
                .assertThat().body("track", notNullValue());// трек-номер не null
    }

    public int getTrack(Response response) {
        return response.then().log().all()
                .extract()
                .path("track");
    }

    @Step("Удаление заказа")
    public void deleteOrder(int track) {
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(Constant.BEARERTOKEN)
                .put(Constant.API_ORDERS + "/cancel?track=" + track)
                .then().log().all()
                .assertThat().statusCode(HTTP_OK);
    }
}

