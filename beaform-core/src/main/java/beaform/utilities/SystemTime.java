package beaform.utilities;

import java.util.Date;

public final class SystemTime {

	private static final TimeSource defaultSrc = () ->  System.currentTimeMillis();

	private static volatile TimeSource source;

	private SystemTime() {
		// Utility classes don't need public constructors.
	}

	public static void reset() {
		source = null;
	}

	public static long getAsLong() {
		return getCurrentTimeSource().getSystemTime();
	}

	public static Date getAsDate() {
		return new Date(getAsLong());
	}

	private static TimeSource getCurrentTimeSource() {
		if (source != null) {
			return source;
		}
		return defaultSrc;
	}

	public static void setTimeSource(TimeSource timeSource) {
		source = timeSource;
	}
}
