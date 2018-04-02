package beaform.commands;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CommandExecutor {

	private static final CommandExecutor INSTANCE = new CommandExecutor();
	private final ExecutorService execPool = Executors.newSingleThreadExecutor();

	public static CommandExecutor getInstance() {
		return INSTANCE;
	}

	public void execute(Command command) {
		this.execPool.submit(() -> command.execute());
	}

	public void shutdown() {
		this.execPool.shutdown();
	}

	public void awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		this.execPool.awaitTermination(timeout, unit);
	}

	public boolean isTerminated() {
		return this.execPool.isTerminated();
	}

}
