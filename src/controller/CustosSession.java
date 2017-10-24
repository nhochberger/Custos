package controller;

import org.apache.log4j.Logger;

import hochberger.utilities.application.ApplicationProperties;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.eventbus.EventBus;
import view.ColorProvider;

public class CustosSession extends BasicSession {

	private final DataStorageProvider dataStorageProvider;
	private final ColorProvider colorProvider;

	public CustosSession(final ApplicationProperties properties, final EventBus eventBus, final Logger logger, final DataStorageProvider dataStorageProvider, final ColorProvider colorProvider) {
		super(properties, eventBus, logger);
		this.dataStorageProvider = dataStorageProvider;
		this.colorProvider = colorProvider;
	}

	public DataStorageProvider getDataStorageProvider() {
		return this.dataStorageProvider;
	}

	public ColorProvider getColorProvider() {
		return this.colorProvider;
	}
}
