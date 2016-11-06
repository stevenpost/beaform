package beaform.importer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

@SuppressWarnings("static-method")
public class ImporterHandlerTest {

	@Test
	public void testHandoverControlForTags() throws SAXException {
		final XMLReader reader = new MockedXmlReader();
		final DefaultHandler handler = new ImporterHandler(reader);
		reader.setContentHandler(handler);

		parseTagsElementStart(handler);

		assertEquals("This isn't the expected handler", TagHandler.class,  reader.getContentHandler().getClass());
	}

	private void parseTagsElementStart(final DefaultHandler handler) throws SAXException {
		handler.startElement("", "", "tags", null);
	}

	@Test
	public void testGainControl() {
		final XMLReader reader = new MockedXmlReader();
		final ImporterHandler handler = new ImporterHandler(reader);
		reader.setContentHandler(new DefaultHandler());

		handler.gainControl();

		assertEquals("This isn't the expected handler", ImporterHandler.class,  reader.getContentHandler().getClass());
	}
}
