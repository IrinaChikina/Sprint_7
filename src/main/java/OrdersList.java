import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrdersList {
    int courierId ;
    String nearestStation;
    int limit = 10;
    int page = 0;

    public int getCourierId() {
        return courierId;
    }

    public String getNearestStation() {
        return nearestStation;
    }

    public int getLimit() {
        return limit;
    }

    public int getPage() {
        return page;
    }

    public OrdersList() {

    }


    @Step("Список из 10 заказов доступных для взятия курьером")
    public Response listActiveOrder10() {
        OrdersList ordersList = new OrdersList();
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(Constant.BEARERTOKEN)
                .body(ordersList)
                .when().get(Constant.API_ORDERS + "?limit=" + getLimit() + "page=" + getPage());
        return response;
    }

    @Step("Список активных и завершенных заказов курьера")
    public Response listOrderCourierId(int courierId) {
        OrdersList ordersList = new OrdersList();
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(Constant.BEARERTOKEN)
                .body(ordersList)
                .when().get(Constant.API_ORDERS + "?courierId=" +courierId);
        return response;
    }

    @Step("Список создан: проверка статуса")
    public void checkStatusCodeListOrder(Response response) {
        response.then().log().all()
                .statusCode(HTTP_OK);
    }
    @Step("В ответе возвращен список заказов")
    public void checkBodyListOrder(Response response) {
        response.then().log().all()
                .assertThat().body("orders", notNullValue());
    }
}

