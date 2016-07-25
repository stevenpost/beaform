package beaform.importer;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public final class Importer {

	private Importer() {
		// A utility class doesn't need a public constructor.
	}

	public static boolean importFromFile(final File input) throws ImporterException {
		final SAXParserFactory factory = SAXParserFactory.newInstance();
		final SAXParser parser;
		try {
			parser = factory.newSAXParser();
		}
		catch (ParserConfigurationException | SAXException e) {
			throw new ImporterException("There was a problem setting up the parser", e);
		}

		final ImporterHandler handler = new ImporterHandler();
		try {
			parser.parse(input, handler);
		}
		catch (SAXException | IOException e) {
			throw new ImporterException("There was a problem parsing the input file", e);
		}

		// The map should be empty now.
		final boolean noPendingIngredients = handler.getPendingIngredients() == 0;
		assert noPendingIngredients;
		return noPendingIngredients;
	}

}
