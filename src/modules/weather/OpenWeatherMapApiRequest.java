package modules.weather;

import hochberger.utilities.files.Closer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import sun.net.www.protocol.http.HttpURLConnection;

public abstract class OpenWeatherMapApiRequest {

    public OpenWeatherMapApiRequest() {
        super();
    }

    protected String baseUrl() {
        return "http://api.openweathermap.org/data/2.5/";
    }

    protected String apiKey() {
        return "beb80c8a5590891c343321ccf26b1a5b";
    }

    protected String unitsValue() {
        return "metric";
    }

    protected String units() {
        return "units=" + unitsValue();
    }

    public String performRequest(final String cityName, final String countryDenominator) throws IOException {
        final String requestUrl = requestType() + cityName + "," + countryDenominator;
        final URL url = new URL(requestUrl);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.connect();

        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        final StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        Closer.close(in);
        connection.disconnect();
        return response.toString();
    }

    protected abstract String requestType();
}
