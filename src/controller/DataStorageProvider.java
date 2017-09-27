package controller;

import java.io.File;

public class DataStorageProvider {

	private static final String USER_DIR = "user.dir";
	private static final String FILE_SEPARATOR = "/";
	private static final String CONFIGURATION_FILE_NAME = "custos.cfg";
	private static final String COLOR_FILE_NAME = "custos-colors.cfg";
	private static final String ALARM_FILE_NAME = "alarms.cfg";

	private final String baseDirectory;

	public DataStorageProvider(final String baseDirectory) {
		super();
		this.baseDirectory = baseDirectory;
	}

	public DataStorageProvider() {
		this(System.getProperty(USER_DIR));
	}

	public String configurationStoragePath() {
		return buildPathFor(CONFIGURATION_FILE_NAME);
	}

	public File configurationStorageFile() {
		return new File(configurationStoragePath());
	}

	public String alarmsStoragePath() {
		return buildPathFor(ALARM_FILE_NAME);
	}

	public File alarmsStorageFile() {
		return new File(alarmsStoragePath());
	}

	private String buildPathFor(final String fileName) {
		return this.baseDirectory + FILE_SEPARATOR + fileName;
	}
}
