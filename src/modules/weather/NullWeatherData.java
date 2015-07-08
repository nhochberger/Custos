package modules.weather;

import java.util.List;

public class NullWeatherData extends WeatherData {

	public NullWeatherData() {
		super();
	}

	@Override
	public City getCity() {
		return super.getCity();
	}

	@Override
	public List<Forecast> getList() {
		// TODO Auto-generated method stub
		return super.getList();
	}
}
