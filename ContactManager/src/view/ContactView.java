package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import model.Contact;
import model.Group;
import model.Model;

public class ContactView implements Observer {
	protected JTextField firstNameField, lastNameField, cityField;
	protected JTable phoneTable;
	protected DefaultTableModel tableModel;
	protected JButton saveBtn, cancelBtn;
	protected JPanel mainPanel, groupPanel;
	protected Contact targettedContact;
	protected ArrayList<Group> contactGroups;// no need in this class just for heritage
	private Model controller;

	public ContactView(String t, Model model) {
		this.controller = model;
		model.addObserver(this);
		contactGroups = new ArrayList<>();
		mainPanel = new JPanel();
		mainPanel.setSize(600, 450);
		mainPanel.setLayout(null);
		mainPanel.setBackground(new Color(30, 30, 30)); // Dark background

		JLabel title = new JLabel(t);
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Arial", Font.BOLD, 20));
		title.setBounds(250 - 100, 10, 200, 30);
		mainPanel.add(title);

		JLabel firstNameLabel = createLabel("First name", 60);
		mainPanel.add(firstNameLabel);
		firstNameField = createTextField(60);
		mainPanel.add(firstNameField);

		JLabel lastNameLabel = createLabel("Last name", 100);
		mainPanel.add(lastNameLabel);
		lastNameField = createTextField(100);
		mainPanel.add(lastNameField);

		JLabel cityLabel = createLabel("City", 140);
		mainPanel.add(cityLabel);
		cityField = createTextField(140);
		mainPanel.add(cityField);

		JLabel phoneLabel = createSpecialLabel("Phone numbers", 180);
		mainPanel.add(phoneLabel);

		tableModel = new DefaultTableModel(new String[] { "Region Code", "Phone Number" }, 5) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		phoneTable = new JTable(tableModel);
		phoneTable.setBackground(new Color(50, 50, 50));
		phoneTable.setForeground(Color.WHITE);
		phoneTable.setGridColor(Color.GRAY);
		phoneTable.setSelectionBackground(new Color(70, 70, 70));
		JScrollPane scrollPane = new JScrollPane(phoneTable);
		scrollPane.setBounds(250 - 20 - 350 / 2, 210, 350, 100);
		mainPanel.add(scrollPane);

		JLabel groupLabel = createSpecialLabel("Add to Groups", 320);
		mainPanel.add(groupLabel);

		groupPanel = new JPanel();
		groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
		groupPanel.setBackground(new Color(30, 30, 30)); // match dark theme
		loadGroups();

		JScrollPane groupScrollPane = new JScrollPane(groupPanel);
		groupScrollPane.setBounds(180, 360, 340, 100);
		groupScrollPane.setBorder(null);
		groupScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		groupScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		mainPanel.add(groupScrollPane);

		saveBtn = createButton("Save", 120, 510, new Color(50, 150, 255));
		saveBtn.setVisible(false);
		int x = 180;
		if (this instanceof ContactUpdate || this instanceof ContactCreate) {
			x += 70;
		}
		cancelBtn = createButton("Cancel", x, 510, new Color(255, 70, 70));
		mainPanel.add(saveBtn);
		mainPanel.add(cancelBtn);

		cancelBtn.addActionListener(_ -> {
			int confirm = JOptionPane.showConfirmDialog(mainPanel, "Do you want to leave this window?",
					"Confirm message", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				model.show("cdb");
			}
		});
	}

	private JLabel createSpecialLabel(String text, int y) {
		JLabel label = new JLabel(text);
		label.setForeground(Color.WHITE);
		label.setBounds(250 - 70, y, 120, 35);
		return label;
	}

	public void setTargetedContact(Contact c) {
		// Always point to the newly selected Contact
		this.targettedContact = c;
		setFields();
	}

	// margin between labels & txtfields = 40
	// label w = 120;
	// field w = 200
	// total w = 360
	private JLabel createLabel(String text, int y) {
		JLabel label = new JLabel(text);
		label.setForeground(Color.WHITE);
		label.setBounds(250 - 70 - 120, y, 120, 35);
		return label;
	}

	private JTextField createTextField(int y) {
		JTextField tf = new JTextField();
		tf.setBounds(250 - 100 + 50, y, 200, 25);
		tf.setBackground(new Color(50, 50, 50));
		tf.setForeground(Color.WHITE);
		tf.setCaretColor(Color.WHITE);
		tf.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		tf.setEditable(false);
		return tf;
	}

	public void setEnabled() {
		firstNameField.setEditable(true);
		lastNameField.setEditable(true);
		cityField.setEditable(true);
		DefaultTableModel tableModel = (DefaultTableModel) phoneTable.getModel();
		DefaultTableModel newTableModel = new DefaultTableModel(tableModel.getDataVector(),
				getColumnNames(tableModel)) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return true;
			}
		};
		newTableModel.setRowCount(5);
		phoneTable.setModel(newTableModel);
		for (Component comp : this.groupPanel.getComponents()) {
			if (comp instanceof JCheckBox) {
				JCheckBox check = (JCheckBox) comp;
				check.setEnabled(true);
			}
		}
	}

	private Vector<String> getColumnNames(DefaultTableModel model) {
		Vector<String> names = new Vector<>();
		for (int i = 0; i < model.getColumnCount(); i++) {
			names.add(model.getColumnName(i));
		}
		return names;
	}

	private JButton createButton(String text, int x, int y, Color color) {
		JButton btn = new JButton(text);
		btn.setBounds(x, y, 100, 30);
		btn.setBackground(color);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setBorder(BorderFactory.createEmptyBorder());
		return btn;
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	public void setFields() {
		// groups
		loadGroups();
		if (targettedContact == null) {
			clearFileds();
			return;
		}
		firstNameField.setText(targettedContact.getFirstName());
		lastNameField.setText(targettedContact.getLastName());
		cityField.setText(targettedContact.getCity());
		// clear it
		tableModel.setRowCount(0);
		tableModel.setRowCount(5);
		int i = 0;
		for (String number : targettedContact.getNumbers()) {
			if (i < 5) {
				String region = number.substring(0, 2);
				String num = number.substring(2);
				tableModel.setValueAt(region, i, 0);
				tableModel.setValueAt(num, i, 1);
			}
			i++;
		}
	}

	private void clearFileds() {
		firstNameField.setText("");
		lastNameField.setText("");
		cityField.setText("");
		// clear it
		tableModel.setRowCount(0);
		tableModel.setRowCount(5);
	}

	private void loadGroups() {
		contactGroups.clear();
		contactGroups.addAll(controller.getGroupsOf(targettedContact));
		ArrayList<Group> allGroups = controller.getGroups();
		groupPanel.removeAll();
		for (Group group : allGroups) {
			JCheckBox check = new JCheckBox(group.getName());
			check.setName("" + group.getId());
			check.setForeground(Color.WHITE);
			check.setBackground(new Color(30, 30, 30));
			check.setSelected(contactGroups.contains(group));
			check.setEnabled(false);
			check.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						contactGroups.add(controller.getGroupOf(Integer.parseInt(check.getName())));
					} else if (e.getStateChange() == ItemEvent.DESELECTED) {
						contactGroups.remove(controller.getGroupOf(Integer.parseInt(check.getName())));
					}
				}
			});
			groupPanel.add(check);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		setFields();
	}
}
