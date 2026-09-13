package view;

import javax.swing.JOptionPane;

import model.Model;

public class ContactUpdate extends ContactView {

	public ContactUpdate(Model model) {
		super("Update Contact", model);
		this.saveBtn.setVisible(true);
		this.saveBtn.addActionListener(_ -> {
			int confirm = JOptionPane.showConfirmDialog(mainPanel, "Do you want to save these changes?",
					"Confirm message", JOptionPane.YES_NO_OPTION);
			if (phoneTable.isEditing()) {
				phoneTable.getCellEditor().stopCellEditing();
			}
			int newRow = tableModel.getRowCount() - 1;
			// Properly notify the table
			tableModel.fireTableRowsInserted(newRow, newRow);
			// Make sure the row is visible
			phoneTable.scrollRectToVisible(phoneTable.getCellRect(newRow, 0, true));
			// Clear any selections
			phoneTable.clearSelection();
			// Force UI update
			phoneTable.repaint();
			if (confirm == JOptionPane.YES_OPTION) {
				model.updatetargettedcontact(firstNameField.getText(), lastNameField.getText(), cityField.getText(),
						tableModel, contactGroups, targettedContact);
				model.show("cdb");
			}
		});

	}
}
