package modules.weather;

import hochberger.utilities.application.session.BasicSession;

import java.util.Timer;
import java.util.TimerTask;

import modules.CustosModuleWidget;
import modules.VisibleCustosModule;
import view.ColorProvider;

public class Weather extends VisibleCustosModule {

	private final Timer timer;
	private final WeatherWidget widget;

	public Weather(final BasicSession session, final ColorProvider colorProvider) {
		super(session, colorProvider);
		this.timer = new Timer();
		this.widget = new WeatherWidget();
	}

	@Override
	public CustosModuleWidget getWidget() {
		return this.widget;
	}

	@Override
	public void updateWidget() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
		this.timer.cancel();
	}

	private class JsonWeatherRequestTimerTask extends TimerTask {

		@Override
		public void run() {

		}
	}
}
