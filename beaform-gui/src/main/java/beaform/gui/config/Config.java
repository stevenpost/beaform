package beaform.gui.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {

	public static final String WINDOW_WIDTH = "main.window.width";
	public static final String WINDOW_HEIGHT= "main.window.height";

	private static final String DEFAULT_CONF_LOCATION = "/beaform.conf";

	private final Properties properties;

	public Config() throws ConfigurationException {
		this.properties = readConfigFromJar(DEFAULT_CONF_LOCATION);
	}

	public Config(String filename) throws ConfigurationException {
		this.properties = readConfigFromFileSystem(filename);
	}

	private static Properties readConfigFromFileSystem(final String filename) throws ConfigurationException {
		Properties readingProperties = new Properties();

		final File configFile = new File(filename);
		Path path = configFile.toPath();
		try(final Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			readingProperties.load(reader);
		}
		catch (IOException e) {
			throw new ConfigurationException(e);
		}

		return readingProperties;
	}

	private Properties readConfigFromJar(final String filename) throws ConfigurationException {
		Properties readingProperties = new Properties();

		final Charset utf8 = StandardCharsets.UTF_8;
		final InputStream fileStream = getClass().getResourceAsStream(filename);
		try(final Reader reader = new BufferedReader(new InputStreamReader(fileStream, utf8))) {
			readingProperties.load(reader);
		}
		catch (IOException e) {
			throw new ConfigurationException(e);
		}

		return readingProperties;
	}

	public Object getProperty(final String key) {
		return this.properties.get(key);
	}

	public int getIntProperty(final String key) {
		return Integer.parseInt((String) this.properties.get(key));
	}

	public boolean containsKey(final String key) {
		return this.properties.containsKey(key);
	}

}
