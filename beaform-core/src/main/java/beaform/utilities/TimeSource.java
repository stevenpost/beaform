package beaform.utilities;

@FunctionalInterface
public interface TimeSource {
	long getSystemTime();
}
