package beaform.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import beaform.GraphDbHandlerForJTA;
import beaform.entities.Base;
import beaform.entities.Formula;

public class FillDbEvent implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		TransactionManager tm = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		try {
			tm.begin();
		}
		catch (NotSupportedException | SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		final EntityManager em = GraphDbHandlerForJTA.getInstance().getNewEntityManager();


		Base base1 = new Base();
		base1.setName("Base123");
		base1.setDescription("First test base");

		em.persist(base1);

		System.out.println(base1.getDescription());

		Base base2 = new Base();
		base2.setName("Base456");
		base2.setDescription("Second test base");

		em.persist(base2);

		Formula form1 = new Formula();
		form1.setName("Form1");
		form1.setDescription("First test formula");

		em.persist(form1);

		Formula form2 = new Formula();
		form2.setName("Form2");
		form2.setDescription("Second test formula");

		em.persist(form2);

		//		// Add relationships
		//		try ( Transaction tx = graphDb.beginTx()) {
		//			Relationship relationship;
		//
		//			relationship = firstFormula.createRelationshipTo( secondBase, RelTypes.CONTAINS );
		//			relationship.setProperty( "amount", "10%" );
		//
		//			relationship = secondFormula.createRelationshipTo( firstBase, RelTypes.CONTAINS );
		//			relationship.setProperty( "amount", "50%" );
		//
		//			relationship = secondFormula.createRelationshipTo( firstFormula, RelTypes.CONTAINS );
		//			relationship.setProperty( "amount", "50%" );
		//
		//			tx.success();
		//		}

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

		System.out.println("stored");

	}

}
