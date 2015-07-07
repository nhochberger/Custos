package modules.weather;

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
}
