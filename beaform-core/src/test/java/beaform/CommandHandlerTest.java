package beaform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import beaform.commands.Command;
import beaform.commands.CommandExecutor;

public class CommandHandlerTest {

	@Test
	public void testCommandHandler() throws InterruptedException {
		CommandExecutor commandExec = CommandExecutor.getInstance();

		MockCommand command = new MockCommand();
		commandExec.execute(command);
		commandExec.shutdown();
		commandExec.awaitTermination(5,TimeUnit.SECONDS);
		assertEquals("This method was not executed as many time as it should", 1, command.getTimesExecuted());
		assertTrue("The command executer is not shut down", commandExec.isTerminated());
	}

	public final class MockCommand implements Command {
		private volatile int timesExecuted = 0;

		public int getTimesExecuted() {
			return this.timesExecuted;
		}

		@Override
		public void execute() {
			this.timesExecuted++;
		}
	}

}
