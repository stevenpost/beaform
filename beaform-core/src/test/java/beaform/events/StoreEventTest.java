package beaform.events;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Test;

import beaform.utilities.SystemTime;
import beaform.utilities.TimeSource;

public class StoreEventTest {

	@After
	public void destroy() {
		SystemTime.reset();
	}

	@Test
	public void testEventString() {
		// Create a 'create' event for a new formula
		String name = "TestFormula";
		Event createEvent = new FormulaCreatedEvent(name);

		SystemTime.setTimeSource(new TimeSource() {
			private final long timeInMillis = System.currentTimeMillis();

			@Override
			public long getSystemTime() {
				return this.timeInMillis;
			}
		});

		Date timestamp = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		// Check if the event converts to a string correctly
		// [timestamp] action properties
		assertEquals("[" + df.format(timestamp) + "] FormulaCreated " + name, createEvent.toEventString());
	}

}
