package modules.weather;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.images.loader.ImageLoader;
import hochberger.utilities.properties.LoadProperties;

import java.io.IOException;
import java.util.Properties;

import javax.swing.Icon;

public class WeatherIconProvider extends SessionBasedObject {

	private Properties iconCodes;

	public WeatherIconProvider(final BasicSession session) {
		super(session);
		this.iconCodes = new Properties();
		try {
			this.iconCodes = LoadProperties.from("modules/weather/iconcodes.cfg");
		} catch (final IOException e) {
			logger().error("Unable to load icon codes", e);
		}
	}

	public Icon getIconForCode(final int code) {
		if (this.iconCodes.containsKey(String.valueOf(code))) {
			return ImageLoader.loadIcon("modules/weather/" + this.iconCodes.getProperty(String.valueOf(code)));
		}
		return ImageLoader.loadIcon("modules/weather/unknown.png");
	}
}
