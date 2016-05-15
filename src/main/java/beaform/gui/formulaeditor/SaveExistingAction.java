package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.GraphDbHandlerForJTA;
import beaform.Ingredient;
import beaform.SearchTagTask;
import beaform.entities.Formula;
import beaform.entities.Tag;

public class SaveExistingAction implements ActionListener {

	private static final Logger LOG = LoggerFactory.getLogger(SaveExistingAction.class);

	private final JTextField txtNameField;
	private final JTextField txtDescriptionField;
	private final ListModel<Tag> lstTags;
	private final ListModel<Ingredient> lstIngredients;
	private final Formula formula;

	public SaveExistingAction(Formula formula, JTextField txtNameField, JTextField txtDescriptionField, ListModel<Ingredient> lstFormulas, ListModel<Tag> lstTags) {
		this.formula = formula;
		this.txtNameField = txtNameField;
		this.txtDescriptionField = txtDescriptionField;
		this.lstIngredients = lstFormulas;
		this.lstTags = lstTags;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LOG.info("Add: " + this.txtNameField.getText() + " with description: " + this.txtDescriptionField.getText());

		TransactionManager tm = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		try {
			tm.begin();
		}
		catch (NotSupportedException | SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		final EntityManager em = GraphDbHandlerForJTA.getInstance().getEntityManagerFactory().createEntityManager();

		this.formula.setName(this.txtNameField.getText());
		this.formula.setDescription(this.txtDescriptionField.getText());
		int nrOfTags = this.lstTags.getSize();
		for (int i = 0; i < nrOfTags; i++) {
			// See if the tag exist in the DB, if so, use it.
			Tag tag = this.lstTags.getElementAt(i);
			Tag pTag = null;
			final Future<Tag> searchresult = GraphDbHandlerForJTA.addTask(new SearchTagTask(tag.getName()));
			try {
				pTag = searchresult.get();
			}
			catch (InterruptedException | ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (pTag == null) {
				em.persist(tag);
				pTag = tag;
			}
			else {
				tag = pTag;
			}
			this.formula.addTag(tag);
		}

		int nrOfIngredients = this.lstIngredients.getSize();
		for (int i = 0; i < nrOfIngredients; i++) {
			// We should only be holding existing Formulas at this point
			Ingredient ingredient = this.lstIngredients.getElementAt(i);
			this.formula.addIngredient(ingredient.getFormula(), ingredient.getAmount());
		}

		em.persist(this.formula);

		em.flush();
		em.close();

		try {
			tm.commit();
		}
		catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
