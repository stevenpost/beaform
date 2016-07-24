package beaform.importer;

import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import beaform.dao.FormulaDAO;
import beaform.entities.Formula;
import beaform.entities.FormulaTag;
import beaform.entities.Ingredient;

public class ImporterHandler extends DefaultHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ImporterHandler.class);

	private boolean inFormula = false;
	private Formula formula = null;
	private boolean inDescription = false;
	private boolean inTotalAmount = false;
	private boolean inTag = false;
	private boolean inIngredient = false;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		System.out.println("Start Element :" + qName);

		switch (qName) {
			case "beaform":
				LOG.debug("Found the root element");
				break;
			case "formulas":
				LOG.debug("Found formulas element");
				break;
			case "formula":
				LOG.debug("Found a formula");
				this.formula = new Formula();
				this.formula.setName(attributes.getValue("name"));
				this.inFormula = true;
				break;
			case "description":
				this.inDescription = true;
				break;
			case "totalAmount":
				this.inTotalAmount = true;
				break;
			case "tags":
				if (this.inFormula) {
					LOG.debug("Starting tags in a formula");
				}
				else {
					LOG.debug("Starting tags outside a formula");
				}
				break;
			case "tag":
				this.inTag = true;
				break;
			case "ingredients":
				LOG.debug("Starting ingredients");
				break;
			case "ingredient":
				this.inIngredient = true;

				break;
			default:
				throw new SAXException("Found unknown element " + qName);
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		System.out.println("End Element :" + qName);
		switch (qName) {
			case "beaform":
				LOG.debug("Found the root element");
				break;
			case "formulas":
				LOG.debug("End formulas element");
				break;
			case "formula":
				LOG.debug("End a formula");
				addFormula();
				this.inFormula = false;
				break;
			case "description":
				this.inDescription = false;
				break;
			case "totalAmount":
				this.inTotalAmount = false;
				break;
			case "tags":
				if (this.inFormula) {
					LOG.debug("End of tags in a formula");
				}
				else {
					LOG.debug("End of tags tags outside a formula");
				}
				break;
			case "tag":
				this.inTag = false;
				break;
			case "ingredients":
				LOG.debug("Starting ingredients");
				break;
			case "ingredient":
				this.inIngredient = true;
				break;
			default:
				throw new SAXException("Found unknown element " + qName);
		}

	}

	private void addFormula() {
		final String name = this.formula.getName();
		final String description = this.formula.getDescription();
		final String totalAmount = this.formula.getTotalAmount();
		final List<Ingredient> ingredients = this.formula.getIngredients();
		final List<FormulaTag> tags = IteratorUtils.toList(this.formula.getTags());
		FormulaDAO.addFormula(name, description, totalAmount, ingredients, tags);
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {

		if (this.inFormula) {
			System.out.println("formula text : " + new String(ch, start, length));
		}

		if (this.inDescription) {
			this.formula.setDescription(new String(ch, start, length));
		}

		if (this.inTotalAmount) {
			this.formula.setTotalAmount(new String(ch, start, length));
		}

		if (this.inTag) {
			if (this.inFormula) {
				this.formula.addTag(new FormulaTag(new String(ch, start, length)));
			}
		}

		if (this.inIngredient) {

		}

	}

}
