package beaform.importer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import beaform.dao.FormulaDAO;
import beaform.dao.NoSuchFormulaException;
import beaform.entities.Formula;
import beaform.entities.FormulaTag;
import beaform.entities.Ingredient;

public class ImporterHandler extends DefaultHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ImporterHandler.class);

	private final Map<String, Map<String, PendingIngredient>> allPendingIngredients = new HashMap<>();

	private boolean inFormula;
	private Formula formula;
	private boolean inDescription;
	private boolean inTotalAmount;
	private boolean inTag;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		LOG.debug("Start Element :" + qName);

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
				handleIngredientStart(attributes);
				break;
			default:
				throw new SAXException("Found unknown element " + qName);
		}

	}

	private void handleIngredientStart(Attributes attributes) {
		final String ingredientName = attributes.getValue("name");
		final String ingredientAmount = attributes.getValue("amount");

		final Formula ingredient = FormulaDAO.findFormulaByName(ingredientName);
		if (ingredient == null) {
			final Map<String, PendingIngredient> currentPending = getPendingMap(ingredientName);
			currentPending.put(this.formula.getName(), new PendingIngredient(ingredientName, ingredientAmount));
		}
		else {
			this.formula.addIngredient(ingredient, ingredientAmount);
		}
	}

	private Map<String, PendingIngredient> getPendingMap(final String ingredientName) {
		final Map<String, PendingIngredient> currentPending;
		if (!this.allPendingIngredients.containsKey(ingredientName)) {
			currentPending = new HashMap<>();
			this.allPendingIngredients.put(ingredientName, currentPending);
		}
		else {
			currentPending = this.allPendingIngredients.get(ingredientName);
		}
		return currentPending;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		LOG.debug("End Element :" + qName);
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
				addPendingIngredients(this.formula.getName());
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
				break;
			default:
				throw new SAXException("Found unknown element " + qName);
		}

	}

	private void addPendingIngredients(String name) {
		if (this.allPendingIngredients.containsKey(name)) {
			final Map<String, PendingIngredient> pendingIngredients = this.allPendingIngredients.get(name);
			for (Entry<String, PendingIngredient> entry : pendingIngredients.entrySet()) {
				final Formula form = FormulaDAO.findFormulaByName(entry.getKey());
				final PendingIngredient pending = entry.getValue();
				final Formula newIngredient = FormulaDAO.findFormulaByName(pending.getName());
				final List<Ingredient> ingredients = FormulaDAO.getIngredients(form);
				ingredients.add(new Ingredient(newIngredient, pending.getAmount()));
				final List<FormulaTag> tags = IteratorUtils.toList(form.getTags());
				try {
					FormulaDAO.updateExisting(form.getName(), form.getDescription(), form.getTotalAmount(), ingredients, tags);
				}
				catch (NoSuchFormulaException e) {
					LOG.error("An unexpected error occured:", e);
				}
			}
			this.allPendingIngredients.remove(name);
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
	public void characters(char[] ch, int start, int length) throws SAXException {

		if (this.inDescription) {
			this.formula.setDescription(new String(ch, start, length));
		}

		if (this.inTotalAmount) {
			this.formula.setTotalAmount(new String(ch, start, length));
		}

		if (this.inTag && this.inFormula) {
			this.formula.addTag(new FormulaTag(new String(ch, start, length)));
		}

	}

	public int getPendingIngredients() {
		return this.allPendingIngredients.size();
	}

}
