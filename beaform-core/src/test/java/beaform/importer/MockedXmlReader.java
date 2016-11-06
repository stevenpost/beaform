package beaform.importer;

import java.io.IOException;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class MockedXmlReader implements XMLReader {

	private ContentHandler contentHandler;

	@Override
	public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setEntityResolver(EntityResolver resolver) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityResolver getEntityResolver() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDTDHandler(DTDHandler handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DTDHandler getDTDHandler() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setContentHandler(ContentHandler handler) {
		this.contentHandler = handler;
	}

	@Override
	public ContentHandler getContentHandler() {
		return this.contentHandler;
	}

	@Override
	public void setErrorHandler(ErrorHandler handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ErrorHandler getErrorHandler() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void parse(InputSource input) throws IOException, SAXException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void parse(String systemId) throws IOException, SAXException {
		throw new UnsupportedOperationException();
	}

}
