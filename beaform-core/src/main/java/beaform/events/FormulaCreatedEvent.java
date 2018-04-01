package beaform.events;

import java.text.SimpleDateFormat;
import java.util.Date;

import beaform.utilities.SystemTime;

public class FormulaCreatedEvent implements Event {
	private static final String EVENT_TYPE = "FormulaCreated";
	private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	private final String name;
	private final long timestamp;

	public FormulaCreatedEvent(String name) {
		this.name = name;
		this.timestamp = SystemTime.getAsLong();
	}

	@Override
	public String toEventString() {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
		final String formattedTimeStamp = dateFormat.format(new Date(this.timestamp));
		return "[" + formattedTimeStamp + "] " + EVENT_TYPE + " " + this.name;
	}

}
