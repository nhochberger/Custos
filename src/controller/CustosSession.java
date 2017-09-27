package controller;

import org.apache.log4j.Logger;

import hochberger.utilities.application.ApplicationProperties;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.eventbus.EventBus;

public class CustosSession extends BasicSession {

	private final DataStorageProvider dataStorageProvider;

	public CustosSession(final ApplicationProperties properties, final EventBus eventBus, final Logger logger, final DataStorageProvider dataStorageProvider) {
		super(properties, eventBus, logger);
		this.dataStorageProvider = dataStorageProvider;
	}

	public DataStorageProvider getDataStorageProvider() {
		return this.dataStorageProvider;
	}
}
