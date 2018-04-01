package beaform.utilities;

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
