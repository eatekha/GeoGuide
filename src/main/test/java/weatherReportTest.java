import com.example.wesgeosys.weatherReport;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class weatherReportTest {

    @Test
    public void testGetCurrentTemperature() {
        // Test if the current temperature is valid (not equal to ERROR_MESSAGE)
        weatherReport.setAllWeatherData();
        assertNotEquals(weatherReport.getCurrentTemperature(), weatherReport.ERROR_MESSAGE);
    }

    @Test
    public void testGetMinTemperature() {
        // Test if the minimum temperature is valid (not equal to ERROR_MESSAGE)
        weatherReport.setAllWeatherData();
        assertNotEquals(weatherReport.getMinTemperature(), weatherReport.ERROR_MESSAGE);
    }

    @Test
    public void testGetMaxTemperature() {
        // Test if the maximum temperature is valid (not equal to ERROR_MESSAGE)
        weatherReport.setAllWeatherData();
        assertNotEquals(weatherReport.getMaxTemperature(), weatherReport.ERROR_MESSAGE);
    }

    @Test
    public void testGetFeelsLikeTemperature() {
        // Test if the "feels like" temperature is valid (not equal to ERROR_MESSAGE)
        weatherReport.setAllWeatherData();
        assertNotEquals(weatherReport.getFeelsLikeTemperature(), weatherReport.ERROR_MESSAGE);
    }
}