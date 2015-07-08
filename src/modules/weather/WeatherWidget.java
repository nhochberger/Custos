package modules.weather;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import modules.CustosModuleWidget;
import net.miginfocom.swing.MigLayout;
import view.ColorProvider;

public class WeatherWidget implements CustosModuleWidget {

	private static final int UNKNOWN = 0;
	private WeatherData weatherData;
	private final WeatherIconProvider iconProvider;
	private boolean isBuilt;
	private final ColorProvider colorProvider;
	private JPanel panel;
	private JLabel weatherStatusLabel;
	private JLabel currentTemperatureLabel;

	public WeatherWidget(final ColorProvider colorProvider, final WeatherIconProvider iconProvider) {
		super();
		this.colorProvider = colorProvider;
		this.iconProvider = iconProvider;
		this.weatherData = new WeatherData();
		this.isBuilt = false;
	}

	@Override
	public void build() {
		if (this.isBuilt) {
			return;
		}
		this.isBuilt = true;
		this.panel = new JPanel(new MigLayout());
		this.panel.setLayout(new MigLayout());
		this.panel.setPreferredSize(new Dimension(300, 100));
		this.panel.setBackground(this.colorProvider.backgroundColor());

		this.currentTemperatureLabel = new JLabel("N/A");
		this.panel.add(this.currentTemperatureLabel);
		this.weatherStatusLabel = new JLabel(this.iconProvider.getIconForCode(UNKNOWN));
		this.panel.add(this.weatherStatusLabel);
	}

	@Override
	public void updateWidget() {
		if (null == this.weatherData) {
			return;
		}
		this.weatherStatusLabel.setIcon(this.iconProvider.getIconForCode(this.weatherData.getList().get(0).getWeather().get(0).getId()));
		this.currentTemperatureLabel.setText(this.weatherData.getList().get(0).getTemp().get("day") + "°");
	}

	@Override
	public JComponent getComponent() {
		return this.panel;
	}

	public void setNewData(final WeatherData weatherData) {
		this.weatherData = weatherData;
	}
}
