import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrdersListTest {

    int courierId;
    String login = RandomStringUtils.random(6,true,true);
    String password = RandomStringUtils.random(4,true,true);

    Couriers couriers = new Couriers(login,password);

    @Step("Запуск Яндекс.Самокат")
    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.URLMETRO;
    }

    @Test
    @DisplayName("Check creating list Orders")
    @Description("GET /api/v1/orders")
    public void createdListOrderTest() {

        OrdersList ordersList = new OrdersList();

        Response response = ordersList.listActiveOrder10();
        ordersList.checkStatusCodeListOrder(response);
        ordersList.checkBodyListOrder(response);

    }
    @Test
    @DisplayName("Check list order for selected courier")
    @Description("GET /api/v1/orders")
    public void createdListOrderForCourierTest() {

        Response response = couriers.sendPostCreatingCourier(couriers);
        couriers.checkCreatedOK(response); // Проверка статуса кода

        var couriersCreated = new AuthorizationCourier(login,password);
        int courierId =  couriersCreated.checkNumberIdCourier(); // Получение Id курьера

        OrdersList ordersList = new OrdersList();

        Response responseList = ordersList.listOrderCourierId(courierId);
        ordersList.checkStatusCodeListOrder(responseList);
        ordersList.checkBodyListOrder(responseList);
    }

    @After
    public void deleteCourier() {
        if (courierId != 0)
            couriers.deleteCourier(courierId);
    }
}
