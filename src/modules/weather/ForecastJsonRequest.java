package modules.weather;

import hochberger.utilities.files.Closer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.joda.time.DateTime;

import sun.net.www.protocol.http.HttpURLConnection;

import com.google.gson.Gson;

public class ForecastJsonRequest extends OpenWeatherMapApiRequest {

	public ForecastJsonRequest() {
		super();
	}

	public String performRequest(final String cityName, final String countryDenominator) throws IOException {
		final String requestUrl = forecastUrl() + cityName + "," + countryDenominator;
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
		final JsonWeatherResult weatherResult = new Gson().fromJson(response.toString(), JsonWeatherResult.class);

		System.err.println(weatherResult.getCity().getName());
		System.err.println(weatherResult.getList().get(0).getTemp().get("day"));
		System.err.println(new DateTime(weatherResult.getList().get(0).getDt() * 1000));
		return response.toString();
	}

	protected String forecastUrl() {
		return baseUrl() + "forecast/daily?" + units() + "&appid=" + apiKey() + "&q=";
	}
}
