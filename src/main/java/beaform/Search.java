package beaform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import beaform.entities.Formula;

public class Search {
	private static final ThreadPoolExecutor tpe = new ThreadPoolExecutor(1, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));
	private static final CompletionService<Iterator<Formula>> retrieveCompService = new ExecutorCompletionService<Iterator<Formula>>(tpe);

	private final String type;

	public Search(String type) {
		this.type = type;
	}

	public Future<Iterator<Formula>> search() {
		GraphDatabaseService graphDb = GraphDbHandler.getInstance().getDbHandle();
		return retrieveCompService.submit(new SearchTask(graphDb, "match (n:" + this.type + ") return n, n.name, n.description"));
	}

	private static final class SearchTask implements Callable<Iterator<Formula>> {
		private final GraphDatabaseService graphDb;
		private final String query;

		public SearchTask(GraphDatabaseService graphDb, String query) {
			this.graphDb = graphDb;
			this.query = query;
		}

		@Override
		public Iterator<Formula> call() throws Exception {
			ArrayList<Formula> resultlist;
			try ( Transaction tx = this.graphDb.beginTx(); Result result = this.graphDb.execute(this.query)) {

				resultlist = new ArrayList<Formula>();
				while (result.hasNext()){
					Map<String,Object> row = result.next();
					Formula form = new Formula(row.get("n.name").toString(), row.get("n.description").toString());
					resultlist.add(form);
				}

			}
			return resultlist.iterator();
		}

	}

}
