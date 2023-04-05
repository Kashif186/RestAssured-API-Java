package API;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

public class APITests {

    private static String token;

    @BeforeClass
    public static void setUp() {
        // Get authentication token
        token = given()
                .contentType("application/json")
                .body("{\"username\":\"" + TestConfig.getAuthUsername() + "\",\"password\":\"" + TestConfig.getAuthPassword() + "\"}")
                .when()
                .post(TestConfig.getBaseUrl() + "/auth")
                .then()
                .extract()
                .path("token");
    }

    @Test
    public void testCreateBooking() {
        String firstname = TestConfig.getBookingFirstname();
        String lastname = TestConfig.getBookingLastname();
        int totalprice = TestConfig.getBookingTotalprice();
        boolean depositpaid = TestConfig.isBookingDepositpaid();
        String checkin = TestConfig.getBookingCheckin();
        String checkout = TestConfig.getBookingCheckout();
        String additionalneeds = TestConfig.getBookingAdditionalneeds();

        given()
                .contentType("application/json")
                .auth()
                .preemptive()
                .basic(TestConfig.getAuthUsername(), TestConfig.getAuthPassword())
                .body("{\"firstname\":\"" + firstname + "\",\"lastname\":\"" + lastname + "\",\"totalprice\":" + totalprice + ",\"depositpaid\":" + depositpaid + ",\"bookingdates\":{\"checkin\":\"" + checkin + "\",\"checkout\":\"" + checkout + "\"},\"additionalneeds\":\"" + additionalneeds + "\"}")
                .when()
                .post(TestConfig.getBaseUrl() + "/booking")
                .then()
                .statusCode(200)
                .body("booking.firstname", equalTo(firstname))
                .body("booking.lastname", equalTo(lastname))
                .body("booking.totalprice", equalTo(totalprice))
                .body("booking.depositpaid", equalTo(depositpaid))
                .body("booking.bookingdates.checkin", equalTo(checkin))
                .body("booking.bookingdates.checkout", equalTo(checkout))
                .body("booking.additionalneeds", equalTo(additionalneeds));
    }

    @Test
    public void testGetBookingById() {
        int bookingid = createBooking();

        given()
                .contentType("application/json")
                .auth()
                .preemptive()
                .basic(TestConfig.getAuthUsername(), TestConfig.getAuthPassword())
                .when()
                .get(TestConfig.getBaseUrl() + "/booking/" + bookingid)
                .then()
                .statusCode(200)
                .body("firstname", equalTo(TestConfig.getBookingFirstname()))
                .body("lastname", equalTo(TestConfig.getBookingLastname()))
                .body("totalprice", equalTo(TestConfig.getBookingTotalprice()))
                .body("depositpaid", equalTo(TestConfig.isBookingDepositpaid()))
                .body("bookingdates.checkin", equalTo(TestConfig.getBookingCheckin()))
                .body("bookingdates.checkout", equalTo(TestConfig.getBookingCheckout()))
                .body("additionalneeds", equalTo(TestConfig.getBookingAdditionalneeds()));
    }

    @Test
    public void testGetAllBookings() {
        createBooking();
        createBooking();
        createBooking();

        given()
                .contentType("application/json")
                .auth()
                .preemptive()
                .basic(TestConfig.getAuthUsername(), TestConfig.getAuthPassword())
                .when()
                .get(TestConfig.getBaseUrl() + "/booking")
                .then()
                .statusCode(200)
                .body("bookings.size()", greaterThanOrEqualTo(2));
    }

    @Test
    public void testUpdateBooking() {
        int bookingid = createBooking();

        String newFirstname = "Jane";
        String newLastname = "Doe";
        int newTotalprice = 500;
        boolean newDepositpaid = false;
        String newCheckin = "2024-05-01";
        String newCheckout = "2024-05-05";
        String newAdditionalneeds = "None";

        given()
                .contentType("application/json")
                .auth()
                .preemptive()
                .basic(TestConfig.getAuthUsername(), TestConfig.getAuthPassword())
                .header("Cookie", "token=" + token)
                .body("{\"firstname\":\"" + newFirstname + "\",\"lastname\":\"" + newLastname + "\",\"totalprice\":" + newTotalprice + ",\"depositpaid\":" + newDepositpaid + ",\"bookingdates\":{\"checkin\":\"" + newCheckin + "\",\"checkout\":\"" + newCheckout + "\"},\"additionalneeds\":\"" + newAdditionalneeds + "\"}")
                .when()
                .put(TestConfig.getBaseUrl() + "/booking/" + bookingid)
                .then()
                .statusCode(200)
                .body("firstname", equalTo(newFirstname))
                .body("lastname", equalTo(newLastname))
                .body("totalprice", equalTo(newTotalprice))
                .body("depositpaid", equalTo(newDepositpaid))
                .body("bookingdates.checkin", equalTo(newCheckin))
                .body("bookingdates.checkout", equalTo(newCheckout))
                .body("additionalneeds", equalTo(newAdditionalneeds));
    }

    @Test
    public void testPartialUpdateBooking() {
        int bookingid = createBooking();

        String newFirstname = "Jane";
        String newLastname = "Doe";
        int newTotalprice = 500;

        given()
                .contentType("application/json")
                .auth()
                .preemptive()
                .basic(TestConfig.getAuthUsername(), TestConfig.getAuthPassword())
                .header("Cookie", "token=" + token)
                .body("{\"firstname\":\"" + newFirstname + "\",\"lastname\":\"" + newLastname + "\",\"totalprice\":" + newTotalprice + "}")
                .when()
                .patch(TestConfig.getBaseUrl() + "/booking/" + bookingid)
                .then()
                .statusCode(200)
                .body("firstname", equalTo(newFirstname))
                .body("lastname", equalTo(newLastname))
                .body("totalprice", equalTo(newTotalprice));
    }

    @Test
    public void testDeleteBooking() {
        int bookingid = createBooking();

        given()
                .contentType("application/json")
                .auth()
                .preemptive()
                .basic(TestConfig.getAuthUsername(), TestConfig.getAuthPassword())
                .header("Cookie", "token=" + token)
                .when()
                .delete(TestConfig.getBaseUrl() + "/booking/" + bookingid)
                .then()
                .statusCode(201);
    }

    @Test
    public void testGetBookingIds() {
        int bookingid1 = createBooking();
        int bookingid2 = createBooking();
        int bookingid3 = createBooking();

        given()
                .contentType("application/json")
                .auth()
                .preemptive()
                .basic(TestConfig.getAuthUsername(), TestConfig.getAuthPassword())
                .when()
                .get(TestConfig.getBaseUrl() + "/booking")
                .then()
                .statusCode(200)
                .body("bookingid", hasItems(bookingid1, bookingid2, bookingid3));
    }

    private int createBooking() {
        String firstname = TestConfig.getBookingFirstname();
        String lastname = TestConfig.getBookingLastname();
        int totalprice = TestConfig.getBookingTotalprice();
        boolean depositpaid = TestConfig.isBookingDepositpaid();
        String checkin = TestConfig.getBookingCheckin();
        String checkout = TestConfig.getBookingCheckout();
        String additionalneeds = TestConfig.getBookingAdditionalneeds();

        Response response = given()
                .contentType("application/json")
                .auth()
                .preemptive()
                .basic(TestConfig.getAuthUsername(), TestConfig.getAuthPassword())
                .body("{\"firstname\":\"" + firstname + "\",\"lastname\":\"" + lastname + "\",\"totalprice\":" + totalprice + ",\"depositpaid\":" + depositpaid + ",\"bookingdates\":{\"checkin\":\"" + checkin + "\",\"checkout\":\"" + checkout + "\"},\"additionalneeds\":\"" + additionalneeds + "\"}")
                .when()
                .post(TestConfig.getBaseUrl() + "/booking");

        return response.then()
                .extract()
                .path("bookingid");
    }




}
