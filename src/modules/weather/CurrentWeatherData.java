package modules.weather;

import java.util.List;

public class CurrentWeatherData {

	private String name;
	private Main main;
	private List<Weather> weather;

	public float getTemperature() {
		return getMain().getTemp();
	}

	public float getMinTemperature() {
		return getMain().getTemp_min();
	}

	public float getMaxTemperature() {
		return getMain().getTemp_max();
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

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Main getMain() {
		return this.main;
	}

	public void setMain(final Main main) {
		this.main = main;
	}

	public CurrentWeatherData() {
		super();
	}

	public class Weather {

		private int id;
		private String icon;
		private String description;

		public int getId() {
			return this.id;
		}

		public void setDescription(final String description) {
			this.description = description;
		}

		public String getDescription() {
			return this.description;
		}

		public void setId(final int id) {
			this.id = id;
		}

		public String getIcon() {
			return this.icon;
		}

		public void setIcon(final String icon) {
			this.icon = icon;
		}

		public Weather() {
			super();
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
