package beaform;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.DbTaskHandler;

/**
 * Test some debugging utilities
 *
 * @author Steven Post
 *
 */
public class DbTaskHandlerTest {

	static final Logger LOG = LoggerFactory.getLogger(DbTaskHandlerTest.class);
	volatile String testString;

	@Test
	public void testCallableAddTask() throws InterruptedException, ExecutionException {
		final Callable<String> task = new Callable<String>() {

			@Override
			public String call() {
				return "testje";
			}
		};
		final Future<String> future = DbTaskHandler.addTask(task);
		final String resultString = future.get();
		assertEquals("This isn't the expected string", "testje", resultString);
	}

	@Test
	public void testRunnableAddTask() throws InterruptedException, ExecutionException {
		final Runnable task = new Runnable() {

			@Override
			public void run() {
				DbTaskHandlerTest.this.testString = "teststring";
			}
		};
		final Future<Void> future = DbTaskHandler.addTask(task);
		future.get();
		assertEquals("This isn't the expected string", "teststring", this.testString);
	}

}
