package beaform.importer;

public class MockedSAXHandlerMaster implements SAXHandlerMaster {

	private boolean hasChangedParser;

	public boolean hasChanged() {
		return this.hasChangedParser;
	}

	@Override
	public void gainControl() {
		this.hasChangedParser = true;
	}

}
