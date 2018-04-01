package beaform.utilities;

import java.util.Date;

public class SystemTime {

	private static final TimeSource defaultSrc = new TimeSource(){
		@Override
		public long getSystemTime() {
			return System.currentTimeMillis();
		}

	};

	private static volatile TimeSource source;

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

	private SystemTime() {
		// Utility classes don't need public constructors.
	}
}
