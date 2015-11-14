package modules.weather;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.geo.GeoInformation;
import hochberger.utilities.geo.GeoInformationProvider;
import hochberger.utilities.text.CommonDateTimeFormatters;
import hochberger.utilities.text.i18n.DirectI18N;
import hochberger.utilities.timing.ToMilis;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import modules.CustosModuleConfiguration;
import modules.CustosModuleWidget;
import modules.VisibleCustosModule;

import org.joda.time.DateTime;

import view.ColorProvider;

import com.google.gson.Gson;

import controller.HeartbeatEvent;
import controller.SystemMessage;
import controller.SystemMessage.MessageSeverity;
import edt.EDT;

public class Weather extends VisibleCustosModule {

	private final Timer timer;
	private final WeatherWidget widget;
	private ForecastData forecastData;
	private CurrentWeatherData currentWeatherData;
	private final WeatherIconProvider iconProvider;
	private final CustosModuleConfiguration configuration;
	private JsonWeatherRequestTimerTask jsonWeatherRequestTimerTask;

	public Weather(final BasicSession session, final ColorProvider colorProvider) {
		super(session, colorProvider);
		this.timer = new Timer();
		this.iconProvider = new WeatherIconProvider(session);
		this.widget = new WeatherWidget(colorProvider, this.iconProvider);
		this.configuration = new CustosModuleConfiguration.NoCustosModuleConfiguration();
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
		this.jsonWeatherRequestTimerTask = new JsonWeatherRequestTimerTask();
		scheduleTask();
		EDT.perform(new Runnable() {

			@Override
			public void run() {
				Weather.this.widget.build();
			}
		});
	}

	private void scheduleTask() {
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
			final GeoInformation geoInformation = new GeoInformationProvider(logger()).retrieveGeoInformation();
			session().getEventBus().publish(
					new SystemMessage(MessageSeverity.NORMAL, new DirectI18N("Location set to: ${0} (${1})", geoInformation.getCity(), geoInformation.getCountryName()).toString()));
			try {
				final String forecastResult = forecastRequest.performRequest(geoInformation.getCity(), geoInformation.getCountryCode());
				Weather.this.forecastData = new Gson().fromJson(forecastResult, ForecastData.class);
				logger().info("Weather data updated");
				session().getEventBus().publish(
						new SystemMessage(MessageSeverity.NORMAL, new DirectI18N("Weather data updated at ${0}.", CommonDateTimeFormatters.hourMinuteSecond().print(DateTime.now())).toString()));
			} catch (final IOException e) {
				session().getLogger().error("Json request was unsuccessful.", e);
				session().getEventBus().publish(new SystemMessage(MessageSeverity.WARNING, "Error while retrieving weather forecast data."));
			}
			try {
				final String currentWeatherResult = currentWeatherRequest.performRequest(geoInformation.getCity(), geoInformation.getCountryCode());
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
		// TODO Auto-generated method stub
	}
}
