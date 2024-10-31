import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;

@RunWith(Parameterized.class)
public  class OrderTest extends OrderCreated {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final List<String> color;

    int track;
    OrderCreated orderCreated = new OrderCreated();

    public OrderTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object [][]order() {
        return new Object[][] {
                {"Ivan", "Petrov", "Mira,1", 1, "89990001122", 1, "12.01.2025", "Встретил в чаще еж ежа", List.of("BLACK")},
                {"Ivan", "Petrov", "Mira,1", 1, "89990001122", 2, "12.01.2025", "Как погода, еж?", List.of("GREY")},
                {"Ivan", "Petrov", "Mira,1", 1, "89990001122", 3, "12.01.2025", "Свежа.", List.of("GREY, BLACK")},
                {"Ivan", "Petrov", "Mira,1", 1, "89990001122", 4, "12.01.2025", "И пошли домой, дрожа,", null},
        };
    }


    @Step("Запуск Яндекс.Самокат")
    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.URLMETRO;
    }

    @Test
    @DisplayName("Order is create")
    @Description("POST /api/v1/orders")
    public void choiceColorInOrder() {
        OrderCreated order = new OrderTest(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = orderCreated.CreatedOrder(order);
        orderCreated.checkStatusCodeCreatedOrder(response);
        orderCreated.checkTrackCreatedOrder(response);
        track = orderCreated.getTrack(response);
    }

    @After
    public void deleteOrder() {
        orderCreated.deleteOrder(track);
    }
}

