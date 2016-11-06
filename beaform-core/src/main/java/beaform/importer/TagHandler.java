package beaform.importer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import beaform.entities.FormulaTag;

public class TagHandler extends DefaultHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ImporterHandler.class);

	private final List<FormulaTag> tags = new ArrayList<>();
	private final SAXHandlerMaster handlerMaster;

	private boolean isInTag;

	public TagHandler(SAXHandlerMaster handlerMaster){
		this.handlerMaster = handlerMaster;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		LOG.debug("Start Element :" + qName);

		if ("tag".equals(qName)) {
			this.isInTag = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
			case "tag":
				this.isInTag = false;
				break;
			case "tags":
				this.handlerMaster.gainControl();
				break;
			default:
				break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (this.isInTag) {
			final String tagName = new String(ch, start, length);
			final FormulaTag newTag = new FormulaTag(tagName);
			this.tags.add(newTag);
		}
	}

	public List<FormulaTag> getTags() {
		return this.tags;
	}

}
