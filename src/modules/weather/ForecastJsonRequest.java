package modules.weather;

public class ForecastJsonRequest extends OpenWeatherMapApiRequest {

    public ForecastJsonRequest() {
        super();
    }

    @Override
    protected String requestType() {
        return baseUrl() + "forecast/daily?" + units() + "&appid=" + apiKey() + "&q=";
    }
}
