package beaform.utilities;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SystemTimeTest {

	@Test
	public void testDefaultTimeSource() {
		final long start = System.currentTimeMillis();
		final long tested = SystemTime.getAsLong();
		final long end = System.currentTimeMillis();

		assertBetween("The tested value is not between the given arguments", start, end, tested);
	}

	private void assertBetween(String message, long boundary1, long boundary2, long tested) {
		long upper, lower;

		if (boundary1 <= boundary2) {
			lower = boundary1;
			upper = boundary2;
		}
		else {
			lower = boundary2;
			upper = boundary1;
		}

		assertTrue(message, (lower <= tested) && (tested <= upper));
	}

}
