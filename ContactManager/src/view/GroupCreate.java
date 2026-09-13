package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import model.Contact;
import model.Group;
import model.Model;

public class GroupCreate implements Observer {

	private JPanel mainPanel;
	protected Model model;
	protected JTextField groupNameField;
	protected JTextField descField;
	protected DefaultTableModel tableModel;
	protected JTable contactTable;
	protected ArrayList<Contact> myContacts;
	protected JButton saveButton;

	public GroupCreate(String subTitle, Model model) {
		this.model = model;
		myContacts = new ArrayList<>();
		model.addObserver(this);
		mainPanel = new JPanel(null);
		mainPanel.setPreferredSize(new Dimension(500, 600));
		mainPanel.setBackground(Color.decode("#121212"));

		// Header
		JLabel headerLabel = new JLabel("Gestion des contacts");
		headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
		headerLabel.setForeground(new Color(0x42A5F5));
		headerLabel.setBounds(150, 10, 300, 30);
		mainPanel.add(headerLabel);

		// Add new group title
		JLabel newGroupTitle = new JLabel(subTitle);
		newGroupTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
		newGroupTitle.setForeground(new Color(0xE53935));
		newGroupTitle.setBounds(190, 50, 200, 20);
		mainPanel.add(newGroupTitle);

		// Group Name Label and Field
		JLabel groupNameLabel = new JLabel("Group name:");
		groupNameLabel.setForeground(Color.WHITE);
		groupNameLabel.setBounds(90, 90, 100, 25);
		mainPanel.add(groupNameLabel);

		groupNameField = new JTextField();
		groupNameField.setBounds(190, 90, 200, 25);
		mainPanel.add(groupNameField);

		// Description Label and Field
		JLabel descLabel = new JLabel("Description:");
		descLabel.setForeground(Color.WHITE);
		descLabel.setBounds(90, 120, 100, 25);
		mainPanel.add(descLabel);

		descField = new JTextField();
		descField.setBounds(190, 120, 200, 25);
		mainPanel.add(descField);

		// Table
		String[] colNames = { "id", "Contact name", "City", "Add to group" };

		tableModel = new DefaultTableModel(colNames, 0) {
			@Override
			public Class<?> getColumnClass(int column) {
				return column == 3 ? Boolean.class : String.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 3;
			}
		};

		contactTable = new JTable(tableModel);
		contactTable.setBackground(new Color(0x1E1E1E));
		contactTable.setForeground(Color.WHITE);
		contactTable.setSelectionBackground(new Color(0x333333));
		contactTable.setSelectionForeground(Color.WHITE);
		contactTable.setGridColor(Color.DARK_GRAY);
		contactTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
		contactTable.setRowHeight(25);
		// hid the id column
		contactTable.getColumnModel().getColumn(0).setMinWidth(0);
		contactTable.getColumnModel().getColumn(0).setMaxWidth(0);
		contactTable.getColumnModel().getColumn(0).setWidth(0);

		tableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				// We only care about updates to a cell, not insert/delete
				if (e.getType() == TableModelEvent.UPDATE) {
					int row = e.getFirstRow();
					int column = e.getColumn();
					// Check if it was the Boolean column (index 2 here)
					if (column == 3) {
						Boolean selected = (Boolean) tableModel.getValueAt(row, column);
						int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
						Contact contact = model.getContactOf(id);
						if (selected) {
							if (!myContacts.contains(contact)) {
								myContacts.add(contact);
							}
						} else {
							if (myContacts.contains(contact)) {
								myContacts.remove(contact);
							}
						}
					}
				}
			}
		});

		// set cell text alignment to center for first 2 columns
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < contactTable.getColumnCount() - 1; i++) {
			contactTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		loadContacts();

		JScrollPane scrollPane = new JScrollPane(contactTable);
		scrollPane.getViewport().setBackground(new Color(0x1E1E1E));
		scrollPane.setBounds(30, 160, 430, 350);
		mainPanel.add(scrollPane);

		// Buttons
		// margin between them = 30
		// total width = 2 * 120 +30 = 270
		final int x = 500 / 2 - 270 / 2;
		saveButton = createBtn("Save Group");
		saveButton.setBounds(x, 520, 120, 35);
		saveButton.setBackground(new Color(0x1E88E5));
		saveButton.addActionListener(_ -> {
			int confirm = JOptionPane.showConfirmDialog(mainPanel, "Do you want to save these changes?",
					"Confirm message", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				Group g = new Group(-1, this.groupNameField.getText(), this.descField.getText(), this.myContacts);
				if (this instanceof GroupUpdate) {// check the subClass first
					Group targeted = ((GroupUpdate) this).getTargetedGroup();
					model.setGroup(targeted, g);
				} else if (this instanceof GroupCreate) {
					model.addGroup(g);
				}
				model.show("gdb");
			}
		});
		mainPanel.add(saveButton);

		JButton cancelButton = createBtn("Cancel");
		cancelButton.setBounds(x + 120 + 30, 520, 120, 35);
		cancelButton.addActionListener(_ -> {
			model.show("gdb");
		});
		mainPanel.add(cancelButton);
	}

	protected void loadContacts() {
		ArrayList<Contact> allContacts = model.getContacts();
		tableModel.setRowCount(0);
		for (Contact contact : allContacts) {
			String name = contact.getFirstName() + " " + contact.getLastName();
			boolean selected = myContacts.contains(contact);
			tableModel.addRow(new Object[] { contact.getId(), name, contact.getCity(), selected });
		}
	}

	private JButton createBtn(String name) {
		JButton btn = new JButton(name);
		btn.setForeground(Color.WHITE);
		btn.setBackground(new Color(237, 73, 86));
		btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btn.setBorder(BorderFactory.createEmptyBorder(4, 15, 4, 15));
		btn.setFocusable(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	@Override
	public void update(Observable o, Object arg) {
		loadContacts();
	}
}
