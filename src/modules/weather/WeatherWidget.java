package modules.weather;

import hochberger.utilities.gui.EnhancedLabel;
import hochberger.utilities.gui.font.FontLoader;
import hochberger.utilities.text.Text;
import hochberger.utilities.text.i18n.DirectI18N;

import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.configuration.WeekdayName;
import modules.CustosModuleWidget;
import net.miginfocom.swing.MigLayout;

import org.joda.time.DateTime;

import view.ColorProvider;

public class WeatherWidget implements CustosModuleWidget {

    private static final String N_A = "n/a ";
    private static final int UNKNOWN = 0;
    private ForecastData forecastData;
    private final WeatherIconProvider iconProvider;
    private boolean isBuilt;
    private final ColorProvider colorProvider;
    private JPanel panel;
    private JLabel weatherStatusLabel;
    private EnhancedLabel cityLabel;
    private EnhancedLabel currentTemperatureLabel;
    private EnhancedLabel minMaxTemperatureLabel;
    private JLabel forecastIconLabel1;
    private EnhancedLabel forecastLabel1;
    private JLabel forecastIconLabel2;
    private EnhancedLabel forecastLabel2;
    private JLabel forecastIconLabel3;
    private EnhancedLabel forecastLabel3;
    private CurrentWeatherData weatherData;

    public WeatherWidget(final ColorProvider colorProvider, final WeatherIconProvider iconProvider) {
        super();
        this.colorProvider = colorProvider;
        this.iconProvider = iconProvider;
        this.forecastData = new ForecastData();
        this.isBuilt = false;
    }

    @Override
    public void build() {
        if (this.isBuilt) {
            return;
        }
        this.isBuilt = true;
        final Font baseFont = FontLoader.loadFromWithFallback("monofonto.ttf", new JPanel().getFont());
        this.panel = new JPanel(new MigLayout());
        this.panel.setLayout(new MigLayout("", "", "5[]5"));
        this.panel.setOpaque(false);

        this.weatherStatusLabel = new JLabel(this.iconProvider.getIconForCode(UNKNOWN));
        this.weatherStatusLabel.setToolTipText(N_A);
        this.panel.add(this.weatherStatusLabel, "dock west");

        final JPanel centerPanel = new JPanel(new MigLayout());
        centerPanel.setOpaque(false);

        this.cityLabel = new EnhancedLabel("n/a");
        this.cityLabel.setFont(baseFont.deriveFont(15f));
        this.cityLabel.setForeground(this.colorProvider.foregroundColor());
        this.cityLabel.setRightShadow(1, 1, this.colorProvider.shadowColor());
        centerPanel.add(this.cityLabel, "wrap");

        this.currentTemperatureLabel = new EnhancedLabel(N_A);
        this.currentTemperatureLabel.setFont(baseFont.deriveFont(50f));
        this.currentTemperatureLabel.setForeground(this.colorProvider.foregroundColor());
        this.currentTemperatureLabel.setRightShadow(1, 1, this.colorProvider.shadowColor());
        centerPanel.add(this.currentTemperatureLabel, "wrap");

        this.minMaxTemperatureLabel = new EnhancedLabel(N_A);
        this.minMaxTemperatureLabel.setFont(baseFont.deriveFont(20f));
        this.minMaxTemperatureLabel.setForeground(this.colorProvider.foregroundColor());
        this.minMaxTemperatureLabel.setRightShadow(1, 1, this.colorProvider.shadowColor());
        centerPanel.add(this.minMaxTemperatureLabel);
        this.panel.add(centerPanel, "dock center");

        final JPanel forecastPanel = new JPanel(new MigLayout("", "", "8[]5[]8"));
        forecastPanel.setOpaque(false);
        this.forecastIconLabel1 = new JLabel(this.iconProvider.getForecastIconFor(UNKNOWN));
        this.forecastLabel1 = new EnhancedLabel(N_A);
        this.forecastLabel1.setFont(baseFont.deriveFont(16f));
        this.forecastLabel1.setRightShadow(1, 1, this.colorProvider.shadowColor());
        this.forecastLabel1.setForeground(this.colorProvider.foregroundColor());
        forecastPanel.add(this.forecastIconLabel1);
        forecastPanel.add(this.forecastLabel1, "wrap");

        this.forecastIconLabel2 = new JLabel(this.iconProvider.getForecastIconFor(UNKNOWN));
        this.forecastLabel2 = new EnhancedLabel(N_A);
        this.forecastLabel2.setFont(baseFont.deriveFont(16f));
        this.forecastLabel2.setRightShadow(1, 1, this.colorProvider.shadowColor());
        this.forecastLabel2.setForeground(this.colorProvider.foregroundColor());
        forecastPanel.add(this.forecastIconLabel2);
        forecastPanel.add(this.forecastLabel2, "wrap");

        this.forecastIconLabel3 = new JLabel(this.iconProvider.getForecastIconFor(UNKNOWN));
        this.forecastLabel3 = new EnhancedLabel(N_A);
        this.forecastLabel3.setFont(baseFont.deriveFont(16f));
        this.forecastLabel3.setRightShadow(1, 1, this.colorProvider.shadowColor());
        this.forecastLabel3.setForeground(this.colorProvider.foregroundColor());
        forecastPanel.add(this.forecastIconLabel3);
        forecastPanel.add(this.forecastLabel3, "wrap");

        this.panel.add(forecastPanel, "dock east");
    }

    @Override
    public void updateWidget() {
        if (null == this.weatherData || null == this.forecastData) {
            return;
        }
        this.cityLabel.setText(this.weatherData.getName());
        this.cityLabel.setForeground(this.colorProvider.foregroundColor());
        this.cityLabel.setRightShadow(1, 1, this.colorProvider.shadowColor());
        this.currentTemperatureLabel.setForeground(this.colorProvider.foregroundColor());
        this.currentTemperatureLabel.setRightShadow(1, 1, this.colorProvider.shadowColor());
        this.minMaxTemperatureLabel.setForeground(this.colorProvider.foregroundColor());
        this.minMaxTemperatureLabel.setRightShadow(1, 1, this.colorProvider.shadowColor());
        this.weatherStatusLabel.setIcon(this.iconProvider.getIconForCode(this.weatherData.getId()));
        this.weatherStatusLabel.setToolTipText(new DirectI18N("Today: ${0}", this.weatherData.getDescription()).toString());
        this.currentTemperatureLabel.setText(Float.valueOf(this.weatherData.getTemperature()).intValue() + "°C");
        final int minTemperature = this.forecastData.getToday().getTemp().get("min").intValue();
        final int maxTemperature = this.forecastData.getToday().getTemp().get("max").intValue();
        this.minMaxTemperatureLabel.setText(minTemperature + "°C - " + maxTemperature + "°C");

        this.forecastIconLabel1.setIcon(this.iconProvider.getForecastIconFor(this.forecastData.getList().get(1).getWeather().get(0).getId()));
        this.forecastIconLabel1.setToolTipText(new DirectI18N("Tomorrow: ${0}", this.forecastData.getList().get(1).getWeather().get(0).getDescription()).toString());
        this.forecastLabel1.setForeground(this.colorProvider.foregroundColor());
        this.forecastLabel1.setRightShadow(1, 1, this.colorProvider.shadowColor());
        this.forecastLabel1.setText(this.forecastData.getList().get(1).getTemp().get("day").intValue() + "°C");

        final int dayOfWeek = DateTime.now().getDayOfWeek();
        final int dayAfterTomorrow = (dayOfWeek + 1) % 7;
        final int twoDaysAfterTomorrow = (dayOfWeek + 2) % 7;
        this.forecastIconLabel2.setIcon(this.iconProvider.getForecastIconFor(this.forecastData.getList().get(2).getWeather().get(0).getId()));
        this.forecastIconLabel2.setToolTipText(new DirectI18N("${0}: ${1}", WeekdayName.getNameFor(dayAfterTomorrow).toString(), this.forecastData.getList().get(2).getWeather().get(0)
                .getDescription()).toString());
        this.forecastLabel2.setForeground(this.colorProvider.foregroundColor());
        this.forecastLabel2.setRightShadow(1, 1, this.colorProvider.shadowColor());
        this.forecastLabel2.setText(this.forecastData.getList().get(2).getTemp().get("day").intValue() + "°C");

        this.forecastIconLabel3.setIcon(this.iconProvider.getForecastIconFor(this.forecastData.getList().get(3).getWeather().get(0).getId()));
        this.forecastIconLabel3.setToolTipText(new DirectI18N("${0}: ${1}", WeekdayName.getNameFor(twoDaysAfterTomorrow).toString(), this.forecastData.getList().get(3).getWeather().get(0)
                .getDescription()).toString());
        this.forecastLabel3.setForeground(this.colorProvider.foregroundColor());
        this.forecastLabel3.setRightShadow(1, 1, this.colorProvider.shadowColor());
        this.forecastLabel3.setText(this.forecastData.getList().get(3).getTemp().get("day").intValue() + "°C");
    }

    @Override
    public JComponent getComponent() {
        return this.panel;
    }

    public void setNewForecastData(final ForecastData forecastData) {
        this.forecastData = forecastData;
    }

    public void setNewCurrentWeatherData(final CurrentWeatherData weatherData) {
        this.weatherData = weatherData;
    }

    @Override
    public String getLayoutConstraints() {
        return Text.empty();
    }
}
