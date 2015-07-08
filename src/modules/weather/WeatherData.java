package modules.weather;

import java.util.List;
import java.util.Map;

public class WeatherData {
	private City city;
	private List<Forecast> list;

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
		private float pressure;
		private float humidity;
		private List<Weather> weather;
		private float speed;
		private int deg;
		private float rain;

		public List<Weather> getWeather() {
			return this.weather;
		}

		public void setWeather(final List<Weather> weather) {
			this.weather = weather;
		}

		public float getSpeed() {
			return this.speed;
		}

		public void setSpeed(final float speed) {
			this.speed = speed;
		}

		public int getDeg() {
			return this.deg;
		}

		public void setDeg(final int deg) {
			this.deg = deg;
		}

		public float getRain() {
			return this.rain;
		}

		public void setRain(final float rain) {
			this.rain = rain;
		}

		public Map<String, Float> getTemp() {
			return this.temp;
		}

		public void setTemp(final Map<String, Float> temp) {
			this.temp = temp;
		}

		public float getPressure() {
			return this.pressure;
		}

		public void setPressure(final float pressure) {
			this.pressure = pressure;
		}

		public float getHumidity() {
			return this.humidity;
		}

		public void setHumidity(final float humidity) {
			this.humidity = humidity;
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

	public static class Temperatures {
		private float day;
		private float min;
		private float max;
		private float night;
		private float eve;
		private float morn;

		public float getDay() {
			return this.day;
		}

		public void setDay(final float day) {
			this.day = day;
		}

		public float getMin() {
			return this.min;
		}

		public void setMin(final float min) {
			this.min = min;
		}

		public float getMax() {
			return this.max;
		}

		public void setMax(final float max) {
			this.max = max;
		}

		public float getNight() {
			return this.night;
		}

		public void setNight(final float night) {
			this.night = night;
		}

		public float getEve() {
			return this.eve;
		}

		public void setEve(final float eve) {
			this.eve = eve;
		}

		public float getMorn() {
			return this.morn;
		}

		public void setMorn(final float morn) {
			this.morn = morn;
		}
	}
}
