package beaform.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import beaform.GraphDbHandler;

public class ListFormulasEvent implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		GraphDbHandler.getInstance().addTask(new Runnable() {

			@Override
			public void run() {
				GraphDatabaseService graphDb = GraphDbHandler.getInstance().getDbHandle();
				String query = "match (n:Formula) return n, n.name, n.description";
				String rows = "";

				try ( Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query)) {
					while (result.hasNext()){
						Map<String,Object> row = result.next();
						for ( Entry<String,Object> column : row.entrySet()) {
							rows += column.getKey() + ": " + column.getValue() + "; ";
						}
						rows += "\n";
					}
				}
				System.out.println("Rows: ");
				System.out.println(rows);
			}
		});

	}
}