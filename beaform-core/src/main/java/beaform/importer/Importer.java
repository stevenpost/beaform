package beaform.importer;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Importer {

	@SuppressWarnings("static-method")
	public void importFromFile(final File input) throws ImporterException {
		final SAXParserFactory factory = SAXParserFactory.newInstance();
		final SAXParser parser;
		final DefaultHandler handler = new ImporterHandler();
		try {
			parser = factory.newSAXParser();
		}
		catch (ParserConfigurationException | SAXException e) {
			throw new ImporterException("There was a problem setting up the parser", e);
		}

		try {
			parser.parse(input, handler);
		}
		catch (SAXException | IOException e) {
			throw new ImporterException("There was a problem parsing the input file", e);
		}
	}

}
