package beaform.commands;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandExecutor {

	private static final CommandExecutor INSTANCE = new CommandExecutor();
	private final ExecutorService execPool = Executors.newSingleThreadExecutor();

	public static CommandExecutor getInstance() {
		return INSTANCE;
	}

	public void execute(Command command) {
		this.execPool.submit(() -> command.execute());
	}

}
