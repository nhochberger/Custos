package modules.weather;

public class CurrentWeatherJsonRequest extends OpenWeatherMapApiRequest {

	public CurrentWeatherJsonRequest() {
		super();
	}

	@Override
	protected String requestType() {
		return baseUrl() + "weather/daily?" + units() + "&appid=" + apiKey() + "&q=";
	}
}
