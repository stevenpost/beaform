package beaform.events;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import beaform.utilities.SystemTime;
import beaform.utilities.TimeSource;

public class StoreEventTest {

	@Before
	public void setup() {
		SystemTime.setTimeSource(new TimeSource() {
			private final long timeInMillis = 0;

			@Override
			public long getSystemTime() {
				return this.timeInMillis;
			}
		});
	}

	@After
	public void destroy() {
		SystemTime.reset();
	}

	@Test
	public void testEventString() {
		// Create a 'create' event for a new formula
		String name = "TestFormula";
		Event createEvent = new FormulaCreatedEvent(name);

		Date timestamp = SystemTime.getAsDate();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		// Check if the event converts to a string correctly
		// [timestamp] action properties
		assertEquals("[" + df.format(timestamp) + "] FormulaCreated " + name, createEvent.toEventString());
	}

}
