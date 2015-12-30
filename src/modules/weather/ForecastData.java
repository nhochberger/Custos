package modules.weather;

import java.util.List;
import java.util.Map;

public class ForecastData {
	private City city;
	private List<Forecast> list;

	public Forecast getToday() {
		return this.list.get(0);
	}

	public Forecast getTodayPlus(final int offset) {
		return this.list.get(offset);
	}

	public City getCity() {
		return this.city;
	}

	public void setCity(final City city) {
		this.city = city;
	}

	public List<Forecast> getList() {
		return this.list;
	}

	public void setList(final List<Forecast> list) {
		this.list = list;
	}

	public static class City {
		private String name;

		public String getName() {
			return this.name;
		}

		public void setName(final String name) {
			this.name = name;
		}
	}

	public static class Forecast {
		private long dt;
		private Map<String, Float> temp;
		private List<Weather> weather;
		private Main main;

		public Main getMain() {
			return this.main;
		}

		public void setMain(final Main main) {
			this.main = main;
		}

		public int getId() {
			return getWeather().get(0).getId();
		}

		public String getDescription() {
			return getWeather().get(0).getDescription();
		}

		public List<Weather> getWeather() {
			return this.weather;
		}

		public void setWeather(final List<Weather> weather) {
			this.weather = weather;
		}

		public Map<String, Float> getTemp() {
			return this.temp;
		}

		public void setTemp(final Map<String, Float> temp) {
			this.temp = temp;
		}

		/**
		 * Note that you have to multiply this value with 1000 in order to
		 * receive proper DateTimes as Java expects timestamps to be presented
		 * in miliseconds
		 *
		 * @return dt from json result
		 */
		public long getDt() {
			return this.dt;
		}

		public void setDt(final long dt) {
			this.dt = dt;
		}
	}

	public static class Weather {
		private int id;
		private String main;
		private String description;
		private String icon;

		public int getId() {
			return this.id;
		}

		public void setId(final int id) {
			this.id = id;
		}

		public String getMain() {
			return this.main;
		}

		public void setMain(final String main) {
			this.main = main;
		}

		public String getDescription() {
			return this.description;
		}

		public void setDescription(final String description) {
			this.description = description;
		}

		public String getIcon() {
			return this.icon;
		}

		public void setIcon(final String icon) {
			this.icon = icon;
		}
	}

	public class Main {

		private float temp;
		private float pressure;
		private int humidity;
		private float temp_min;
		private float temp_max;

		public Main() {
			super();
		}

		public float getTemp() {
			return this.temp;
		}

		public void setTemp(final float temp) {
			this.temp = temp;
		}

		public float getPressure() {
			return this.pressure;
		}

		public void setPressure(final float pressure) {
			this.pressure = pressure;
		}

		public int getHumidity() {
			return this.humidity;
		}

		public void setHumidity(final int humidity) {
			this.humidity = humidity;
		}

		public float getTemp_min() {
			return this.temp_min;
		}

		public void setTemp_min(final float temp_min) {
			this.temp_min = temp_min;
		}

		public float getTemp_max() {
			return this.temp_max;
		}

		public void setTemp_max(final float temp_max) {
			this.temp_max = temp_max;
		}
	}
}
