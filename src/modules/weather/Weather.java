package modules.weather;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.text.CommonDateTimeFormatters;
import hochberger.utilities.text.i18n.DirectI18N;
import hochberger.utilities.timing.ToMilis;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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

    private static final String WEATHER_CITY = "weather.city";
    private static final String WEATHER_COUNTRY = "weather.country";

    private final Timer timer;
    private final WeatherWidget widget;
    private final String city;
    private final String country;
    private ForecastData forecastData;
    private CurrentWeatherData currentWeatherData;
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
                Weather.this.widget.setNewForecastData(Weather.this.forecastData);
                Weather.this.widget.setNewCurrentWeatherData(Weather.this.currentWeatherData);
                getWidget().updateWidget();
            }
        });
    }

    @Override
    public void start() {
        this.timer.schedule(new JsonWeatherRequestTimerTask(), ToMilis.seconds(1.5), ToMilis.minutes(5));
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
            final ForecastJsonRequest forecastRequest = new ForecastJsonRequest();
            final CurrentWeatherJsonRequest currentWeatherRequest = new CurrentWeatherJsonRequest();
            try {
                final String forecastResult = forecastRequest.performRequest(Weather.this.city, Weather.this.country);
                Weather.this.forecastData = new Gson().fromJson(forecastResult, ForecastData.class);
                session().getEventBus().publish(
                        new SystemMessage(MessageSeverity.NORMAL, new DirectI18N("Weather data updated at ${0}.", CommonDateTimeFormatters.hourMinuteSecond().print(DateTime.now())).toString()));
            } catch (final IOException e) {
                session().getLogger().error("Json request was unsuccessful.", e);
                session().getEventBus().publish(new SystemMessage(MessageSeverity.WARNING, "Error while retrieving weather forecast data."));
            }
            try {
                final String currentWeatherResult = currentWeatherRequest.performRequest(Weather.this.city, Weather.this.country);
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
}
