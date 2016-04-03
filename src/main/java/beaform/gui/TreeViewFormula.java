package beaform.gui;

import beaform.entities.Formula;

public class TreeViewFormula {
	Formula form;
	String metadata;

	public TreeViewFormula(Formula form, String metadata) {
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
