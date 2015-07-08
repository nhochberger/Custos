package modules.weather;

import hochberger.utilities.application.session.BasicSession;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import modules.CustosModuleWidget;
import modules.VisibleCustosModule;
import view.ColorProvider;

import com.google.gson.Gson;

import controller.HeartbeatEvent;
import edt.EDT;

public class Weather extends VisibleCustosModule {

	private static final String WEATHER_CITY = "weather.city";
	private static final String WEATHER_COUNTRY = "weather.country";

	private final Timer timer;
	private final WeatherWidget widget;
	private final String city;
	private final String country;
	private WeatherData weatherData;
	private final WeatherIconProvider iconProvider;

	public Weather(final BasicSession session, final ColorProvider colorProvider) {
		super(session, colorProvider);
		this.timer = new Timer();
		this.iconProvider = new WeatherIconProvider(session);
		this.city = session().getProperties().otherProperty(WEATHER_CITY);
		this.country = session().getProperties().otherProperty(WEATHER_COUNTRY);
		this.widget = new WeatherWidget(colorProvider, this.iconProvider);
	}

	@Override
	public CustosModuleWidget getWidget() {
		return this.widget;
	}

	@Override
	public void updateWidget() {
		EDT.performBlocking(new Runnable() {

			@Override
			public void run() {
				Weather.this.widget.setNewData(Weather.this.weatherData);
				getWidget().updateWidget();
			}
		});
	}

	@Override
	public void start() {
		this.timer.schedule(new JsonWeatherRequestTimerTask(), 1500, 5 * 60 * 1000);
		EDT.perform(new Runnable() {

			@Override
			public void run() {
				Weather.this.widget.build();
			}
		});
	}

	@Override
	public void stop() {
		this.timer.cancel();
	}

	private class JsonWeatherRequestTimerTask extends TimerTask {

		public JsonWeatherRequestTimerTask() {
			super();
		}

		@Override
		public void run() {
			final ForecastJsonRequest request = new ForecastJsonRequest();
			try {
				final String result = request.performRequest(Weather.this.city, Weather.this.country);
				Weather.this.weatherData = new Gson().fromJson(result, WeatherData.class);
			} catch (final IOException e) {
				session().getLogger().error("Json request was unsuccessful.", e);
			}
		}
	}

	@Override
	public void receive(final HeartbeatEvent event) {
		updateWidget();
	}
}
