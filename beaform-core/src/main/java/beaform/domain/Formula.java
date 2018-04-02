package beaform.domain;

import java.util.Set;

public interface Formula {

	String getName();
	String getDescription();
	String getNotes();
	Set<Ingredient> getIngredients();
	void addIngredient(Formula ingr1, String amount);
	void setNotes(String formulaNotes);

}
