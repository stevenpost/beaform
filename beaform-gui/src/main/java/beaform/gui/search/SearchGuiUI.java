package beaform.gui.search;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A search GUI.
 *
 * @author Steven Post
 *
 */
public final class SearchGuiUI {

	private final SearchGui searchgui;
	private final JPanel panel = new JPanel(new BorderLayout());
	private final JTextField txtSearch = new JTextField();
	private final DefaultComboBoxModel<SearchType> comboBoxModel = new DefaultComboBoxModel<>(SearchType.values());

	public SearchGuiUI(SearchGui searchgui) {
		this.searchgui = searchgui;
		final JPanel searchPanel =  createSearchPanel();
		this.panel.add(searchPanel, BorderLayout.PAGE_START);
	}

	private JPanel createSearchPanel() {
		final Dimension textFieldSize = new Dimension(100, 30);
		final Dimension textFieldMaxSize = new Dimension(300, 30);

		final JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));

		final JTextField search = this.txtSearch;
		search.setMinimumSize(textFieldSize);
		search.setPreferredSize(textFieldSize);
		search.setMaximumSize(textFieldMaxSize);
		searchPanel.add(search);

		final JComboBox<SearchType> cmbType = new JComboBox<>(this.comboBoxModel);
		cmbType.setMinimumSize(textFieldSize);
		cmbType.setMaximumSize(textFieldMaxSize);
		cmbType.setPreferredSize(textFieldSize);
		cmbType.setEditable(false);
		cmbType.setSelectedIndex(0);
		searchPanel.add(cmbType);

		final JButton btnSearch = new JButton("Search");
		searchPanel.add(btnSearch);
		btnSearch.addActionListener(new SearchAction(this.searchgui));

		return searchPanel;
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public String getSearchText() {
		return this.txtSearch.getText();
	}

	public SearchType getSearchType() {
		return (SearchType) this.comboBoxModel.getSelectedItem();
	}

}
