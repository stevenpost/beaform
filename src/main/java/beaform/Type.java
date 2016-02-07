package beaform;

import org.neo4j.graphdb.Label;

public enum Type {
	FORMULA{
		private final Label label = new Label() {

			@Override
			public String name() {
				return "Formula";
			}

		};

		@Override
		public Label getLabel() {
			return this.label;
		}

	},
	BASE{

		private final Label label = new Label() {

			@Override
			public String name() {
				return "Base";
			}

		};

		@Override
		public Label getLabel() {
			return this.label;
		}

	};

	public abstract Label getLabel();

}
