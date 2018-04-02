package beaform.utilities;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

public class SystemTimeTest {

	@After
	public void destroy() {
		SystemTime.reset();
	}

	@Test
	public void testDefaultTimeSource() {
		SystemTime.reset();
		final long start = System.currentTimeMillis();
		final long tested = SystemTime.getAsLong();
		final long end = System.currentTimeMillis();

		assertTrue("The tested value is not between the given arguments", isBetween(start, end, tested));
	}

	private boolean isBetween(long boundary1, long boundary2, long tested) {
		long upper, lower;

		if (boundary1 <= boundary2) {
			lower = boundary1;
			upper = boundary2;
		}
		else {
			lower = boundary2;
			upper = boundary1;
		}

		return (lower <= tested) && (tested <= upper);
	}

}
