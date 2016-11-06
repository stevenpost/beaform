package beaform.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.xml.sax.SAXException;

import beaform.entities.FormulaTag;

@SuppressWarnings("static-method")
public class TagHandlerTest {

	@Test
	public void testEndTags() throws SAXException {
		final MockedSAXHandlerMaster handlerMaster = new MockedSAXHandlerMaster();
		final TagHandler handler = new TagHandler(handlerMaster);

		handler.endElement("", "", "tags");

		assertTrue("The active handler hasn't changed", handlerMaster.hasChanged());
	}

	@Test
	public void testTagHandler() throws SAXException {
		final String firstTag = "firstTag";
		final String secondTag = "secondTag";
		final TagHandler handler = new TagHandler(new MockedSAXHandlerMaster());

		parseTagElement(firstTag, handler);
		parseTagElement(secondTag, handler);

		List<FormulaTag> newTags = handler.getTags();
		assertEquals("This isn't the expected number of tags", 2, newTags.size());
		assertEquals("This isn't the expected tag", firstTag, newTags.get(0).getName());
		assertEquals("This isn't the expected tag", secondTag, newTags.get(1).getName());
	}

	private void parseTagElement(final String tagName, final TagHandler handler) throws SAXException {
		handler.startElement("", "", "tag", null);
		handler.characters(tagName.toCharArray(), 0, tagName.length());
		handler.endElement("", "", "tag");
	}

}
