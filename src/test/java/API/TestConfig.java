package API;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestConfig {
    private static final String CONFIG_FILE = "src/test/resources/config.properties";
    private static final Properties props = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream(CONFIG_FILE);
            props.load(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file: " + CONFIG_FILE, e);
        }
    }

    public static String getBaseUrl() {
        return props.getProperty("base_url");
    }

    public static String getAuthUsername() {
        return props.getProperty("auth_username");
    }

    public static String getAuthPassword() {
        return props.getProperty("auth_password");
    }

    public static String getBookingFirstname() {
        return props.getProperty("booking_firstname");
    }

    public static String getBookingLastname() {
        return props.getProperty("booking_lastname");
    }

    public static int getBookingTotalprice() {
        return Integer.parseInt(props.getProperty("booking_totalprice"));
    }

    public static boolean isBookingDepositpaid() {
        return Boolean.parseBoolean(props.getProperty("booking_depositpaid"));
    }

    public static String getBookingCheckin() {
        return props.getProperty("booking_checkin");
    }

    public static String getBookingCheckout() {
        return props.getProperty("booking_checkout");
    }

    public static String getBookingAdditionalneeds() {
        return props.getProperty("booking_additionalneeds");
    }
}
