package beaform.utilities;

import java.util.Date;

public class SystemTime {

	private static final TimeSource defaultSrc = new TimeSource(){
		@Override
		public long getSystemTime() {
			return System.currentTimeMillis();
		}

	};

	private static TimeSource source;

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
		return (source == null) ? defaultSrc : source;
	}

	public static void setTimeSource(TimeSource timeSource) {
		source = timeSource;
	}
}
