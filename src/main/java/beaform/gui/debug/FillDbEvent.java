package beaform.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import beaform.GraphDbHandler;
import beaform.RelTypes;
import beaform.entities.Base;
import beaform.entities.Formula;

public class FillDbEvent implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		GraphDatabaseService graphDb = GraphDbHandler.getInstance().getDbHandle();

		Base base1 = new Base("Base123", "First test base");
		Node firstBase = base1.persist(graphDb);
		Base base2 = new Base("Base456", "Second test base");
		Node secondBase = base2.persist(graphDb);

		Formula form1 = new Formula("Form1", "First test formula");
		Node firstFormula = form1.persist(graphDb);
		Formula form2 = new Formula("Form2", "Second test formula");
		Node secondFormula = form2.persist(graphDb);

		// Add relationships
		try ( Transaction tx = graphDb.beginTx()) {
			Relationship relationship;

			relationship = firstFormula.createRelationshipTo( secondBase, RelTypes.CONTAINS );
			relationship.setProperty( "amount", "10%" );

			relationship = secondFormula.createRelationshipTo( firstBase, RelTypes.CONTAINS );
			relationship.setProperty( "amount", "50%" );

			relationship = secondFormula.createRelationshipTo( firstFormula, RelTypes.CONTAINS );
			relationship.setProperty( "amount", "50%" );

			tx.success();
		}
	}

}
