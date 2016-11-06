package beaform.importer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public final class Importer {

	private static final File SCHEMA = new File("xml-schemas/input.xsd");

	private Importer() {
		// A utility class doesn't need a public constructor.
	}

	public static boolean importFromFile(final File input) throws ImporterException {

		validateXmlFile(input);

		final XMLReader reader;
		try {
			reader = XMLReaderFactory.createXMLReader();
		}
		catch (SAXException e) {
			throw new ImporterException("There was a problem setting up the parser", e);
		}

		final ImporterHandler handler = new ImporterHandler();
		try (InputStream inputStream = new BufferedInputStream(new FileInputStream(input))) {
			reader.setContentHandler(handler);
			reader.parse(new InputSource(inputStream));

		}
		catch (SAXException | IOException e) {
			throw new ImporterException("There was a problem parsing the input file", e);
		}

		// The map should be empty now.
		final boolean noPendingIngredients = handler.getPendingIngredients() == 0;
		assert noPendingIngredients;
		return noPendingIngredients;
	}

	private static void validateXmlFile(final File input) throws ImporterException {
		SchemaFactory schemaFactory = SchemaFactory
						.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema schema = schemaFactory.newSchema(SCHEMA);
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			saxFactory.setSchema(schema);
			SAXParser parser = saxFactory.newSAXParser();
			parser.parse(input, new DefaultHandler() {
				@Override
				public void error(SAXParseException e) throws SAXException {
					throw e;
				}
			});
		}
		catch (SAXException | IOException | ParserConfigurationException e) {
			throw new ImporterException("Unable to validate the XML", e);
		}
	}

}
