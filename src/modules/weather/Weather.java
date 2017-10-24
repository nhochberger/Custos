package modules.weather;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;

import com.google.gson.Gson;

import controller.CustosSession;
import controller.HeartbeatEvent;
import controller.SystemMessage;
import controller.SystemMessage.MessageSeverity;
import edt.EDT;
import hochberger.utilities.geo.GeoInformation;
import hochberger.utilities.geo.GeoInformationProvider;
import hochberger.utilities.text.CommonDateTimeFormatters;
import hochberger.utilities.text.i18n.DirectI18N;
import hochberger.utilities.timing.ToMilis;
import model.configuration.CustosModuleConfiguration;
import modules.CustosModuleWidget;
import modules.VisibleCustosModule;

public class Weather extends VisibleCustosModule {

	private final Timer timer;
	private final WeatherWidget widget;
	private ForecastData forecastData;
	private CurrentWeatherData currentWeatherData;
	private final WeatherIconProvider iconProvider;
	private final WeatherConfiguration configuration;
	private JsonWeatherRequestTimerTask jsonWeatherRequestTimerTask;
	private final CountryCity countryCity;

	public Weather(final CustosSession session) {
		super(session);
		this.timer = new Timer();
		this.iconProvider = new WeatherIconProvider(session);
		this.widget = new WeatherWidget(colorProvider(), this.iconProvider);
		this.configuration = new WeatherConfiguration();
		this.countryCity = new CountryCity();
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
				Weather.this.widget.setNewForecastData(Weather.this.forecastData);
				Weather.this.widget.setNewCurrentWeatherData(Weather.this.currentWeatherData);
				getWidget().updateWidget();
			}
		});
	}

	@Override
	public void start() {
		logger().info("Starting Weather");
		scheduleTask();
		EDT.perform(new Runnable() {

			@Override
			public void run() {
				Weather.this.widget.build();
			}
		});
	}

	private void scheduleTask() {
		if (null != this.jsonWeatherRequestTimerTask) {
			this.jsonWeatherRequestTimerTask.cancel();
			this.timer.purge();
		}
		this.jsonWeatherRequestTimerTask = new JsonWeatherRequestTimerTask();
		this.timer.schedule(this.jsonWeatherRequestTimerTask, ToMilis.seconds(1.5), ToMilis.minutes(30));
	}

	@Override
	public void stop() {
		this.timer.cancel();
		logger().info("Weather stopped");
	}

	private class JsonWeatherRequestTimerTask extends TimerTask {

		public JsonWeatherRequestTimerTask() {
			super();
		}

		@Override
		public void run() {
			final ForecastJsonRequest forecastRequest = new ForecastJsonRequest();
			final CurrentWeatherJsonRequest currentWeatherRequest = new CurrentWeatherJsonRequest();
			if (Weather.this.configuration.automatic()) {
				final GeoInformation geoInformation = new GeoInformationProvider(logger()).retrieveGeoInformation();
				Weather.this.countryCity.setCountry(geoInformation.getCountryCode());
				Weather.this.countryCity.setCity(geoInformation.getCity());
			}
			try {
				final String forecastResult = forecastRequest.performRequest(Weather.this.countryCity.city(), Weather.this.countryCity.country());
				Weather.this.forecastData = new Gson().fromJson(forecastResult, ForecastData.class);
				logger().info("Weather data updated");
				session().getEventBus().publish(new SystemMessage(MessageSeverity.NORMAL, new DirectI18N("Weather data for ${0} (${1}) updated at ${2}.", Weather.this.countryCity.city(),
						Weather.this.countryCity.country(), CommonDateTimeFormatters.hourMinuteSecond().print(DateTime.now())).toString()));
			} catch (final IOException e) {
				session().getLogger().error("Json request was unsuccessful.", e);
				session().getEventBus().publish(new SystemMessage(MessageSeverity.WARNING, "Error while retrieving weather forecast data."));
			}
			try {
				final String currentWeatherResult = currentWeatherRequest.performRequest(Weather.this.countryCity.city(), Weather.this.countryCity.country());
				Weather.this.currentWeatherData = new Gson().fromJson(currentWeatherResult, CurrentWeatherData.class);
			} catch (final IOException e) {
				session().getLogger().error("Json request was unsuccessful.", e);
				session().getEventBus().publish(new SystemMessage(MessageSeverity.WARNING, "Error while retrieving current weather data."));
			}
		}
	}

	@Override
	public void receive(final HeartbeatEvent event) {
		// do nothing on purpose here
	}

	@Override
	public CustosModuleConfiguration getConfiguration() {
		return this.configuration;
	}

	@Override
	public void applyConfiguration() {
		if (!this.configuration.automatic()) {
			this.countryCity.setCountry(this.configuration.country());
			this.countryCity.setCity(this.configuration.city());
		}
		scheduleTask();
	}
}
