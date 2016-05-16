package beaform.gui;

import beaform.entities.Formula;

public class TreeViewFormula {
	private final Formula form;
	private final String metadata;

	public TreeViewFormula(Formula form, String metadata) {
		if (metadata == null) {
			metadata = "";
		}
		this.form = form;
		metadata = metadata.substring(metadata.indexOf('|') + 1);
		this.metadata = metadata;
	}

	@Override
	public String toString() {
		return this.form.getName();
	}

	public Formula getFormula() {
		return this.form;
	}

	public String getMetadata() {
		return this.metadata;
	}

}
