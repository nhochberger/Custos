package modules.weather;

public class CurrentWeatherJsonRequest extends OpenWeatherMapApiRequest {

	public CurrentWeatherJsonRequest() {
		super();
	}

	@Override
	protected String requestType() {
		return baseUrl() + "weather?" + units() + "&appid=" + apiKey() + "&q=";
	}
}
