package modules.weather;

import hochberger.utilities.gui.EnhancedLabel;
import hochberger.utilities.gui.font.FontLoader;

import java.awt.Dimension;
import java.awt.Font;

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
	private EnhancedLabel cityLabel;
	private EnhancedLabel currentTemperatureLabel;
	private EnhancedLabel minMaxTemperatureLabel;

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
		Font baseFont = FontLoader.loadFrom("monofonto.ttf");
		if (null == baseFont) {
			baseFont = new JPanel().getFont();
		}
		this.panel = new JPanel(new MigLayout());
		this.panel.setLayout(new MigLayout("", "10[]10[]10", "10[]1[]1[]:"));
		this.panel.setPreferredSize(new Dimension(300, 100));
		this.panel.setBackground(this.colorProvider.backgroundColor());

		this.weatherStatusLabel = new JLabel(this.iconProvider.getIconForCode(UNKNOWN));
		this.panel.add(this.weatherStatusLabel, "dock west");

		this.cityLabel = new EnhancedLabel("n/a");
		this.cityLabel.setFont(baseFont.deriveFont(15f));
		this.cityLabel.setForeground(this.colorProvider.foregroundColor());
		this.cityLabel.setRightShadow(1, 1, this.colorProvider.shadowColor());
		this.panel.add(this.cityLabel, "wrap");

		this.currentTemperatureLabel = new EnhancedLabel("n/a ");
		this.currentTemperatureLabel.setFont(baseFont.deriveFont(50f));
		this.currentTemperatureLabel.setForeground(this.colorProvider.foregroundColor());
		this.currentTemperatureLabel.setRightShadow(1, 1, this.colorProvider.shadowColor());
		this.panel.add(this.currentTemperatureLabel, "wrap");

		this.minMaxTemperatureLabel = new EnhancedLabel("n/a");
		this.minMaxTemperatureLabel.setFont(baseFont.deriveFont(20f));
		this.minMaxTemperatureLabel.setForeground(this.colorProvider.foregroundColor());
		this.minMaxTemperatureLabel.setRightShadow(1, 1, this.colorProvider.shadowColor());
		this.panel.add(this.minMaxTemperatureLabel);
	}

	@Override
	public void updateWidget() {
		if (null == this.weatherData) {
			return;
		}
		this.cityLabel.setText(this.weatherData.getCity().getName());
		this.weatherStatusLabel.setIcon(this.iconProvider.getIconForCode(this.weatherData.getList().get(0).getWeather().get(0).getId()));
		this.currentTemperatureLabel.setText(this.weatherData.getList().get(0).getTemp().get("day").intValue() + "°C");
		final int minTemperature = this.weatherData.getList().get(0).getTemp().get("min").intValue();
		final int maxTemperature = this.weatherData.getList().get(0).getTemp().get("max").intValue();
		this.minMaxTemperatureLabel.setText(minTemperature + "°C - " + maxTemperature + "°C");
	}

	@Override
	public JComponent getComponent() {
		return this.panel;
	}

	public void setNewData(final WeatherData weatherData) {
		this.weatherData = weatherData;
	}
}
