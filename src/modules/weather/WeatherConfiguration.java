package modules.weather;

import hochberger.utilities.text.i18n.DirectI18N;
import model.configuration.CustosModuleBooleanConfigurationEntry;
import model.configuration.CustosModuleConfiguration;
import model.configuration.CustosModuleStringConfigurationEntry;

public class WeatherConfiguration extends CustosModuleConfiguration {
	private static final String DEFAULT_COUNTRY = "de";
	private static final String DEFAULT_CITY = "Berlin";
	private static final String WEATHER_CITY = "weather.city";
	private static final String WEATHER_COUNTRY = "weather.country";
	private static final String WEATHER_AUTOMATIC = "weather.automatic";
	private static final Boolean DEFAULT_AUTOMATIC = Boolean.TRUE;

	private final CustosModuleBooleanConfigurationEntry automaticEntry;
	private final CustosModuleStringConfigurationEntry cityEntry;
	private final CustosModuleStringConfigurationEntry countryEntry;

	public WeatherConfiguration() {
		super(new DirectI18N("Weather Settings"));
		this.automaticEntry = new CustosModuleBooleanConfigurationEntry(new DirectI18N("Automatic location:"), new DirectI18N(
				"Check, if your location sould be determined automatically. Note that with this does not work reliably with some internet connections. Use the below fields for fallback values."),
				WEATHER_AUTOMATIC, DEFAULT_AUTOMATIC);
		this.countryEntry = new CustosModuleStringConfigurationEntry(new DirectI18N("Country:"), new DirectI18N("The country in which you are."), WEATHER_COUNTRY, DEFAULT_COUNTRY);
		this.cityEntry = new CustosModuleStringConfigurationEntry(new DirectI18N("City:"), new DirectI18N("The city in which you are."), WEATHER_CITY, DEFAULT_CITY);
		addConfigurationEntry(this.automaticEntry);
		addConfigurationEntry(this.countryEntry);
		addConfigurationEntry(this.cityEntry);
	}

	public boolean automatic() {
		return this.automaticEntry.getValue().booleanValue();
	}

	public String country() {
		return this.countryEntry.getValue();
	}

	public String city() {
		return this.cityEntry.getValue();
	}
}
