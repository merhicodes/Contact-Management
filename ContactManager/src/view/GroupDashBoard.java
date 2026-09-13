package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import model.Contact;
import model.Group;
import model.Model;

public class GroupDashBoard implements Observer {

	private JPanel mainPanel;
	private Cursor handCursor;
	private DefaultListModel<Group> groupListModel;
	private DefaultTableModel tableModel;
	private Group targetedGroup;
	private JTable contactTable;
	private Model model;
	private ArrayList<Group> groups;

	public GroupDashBoard(Model model) {
		this.model = model;
		model.addObserver(this);
		groups = new ArrayList<>();
//		model.addModelObserver(this);
		mainPanel = new JPanel(null);
		mainPanel.setBackground(new Color(18, 18, 18));

		// Title label
		JLabel title = new JLabel("Groups");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Arial", Font.BOLD, 16));
		title.setBounds(20, 10, 100, 30);
		mainPanel.add(title);

		// Add Group button
		handCursor = new Cursor(Cursor.HAND_CURSOR);
		JButton addGroupButton = createBtn("+");
		addGroupButton.addActionListener(_ -> {
			model.show("gc");
		});
		addGroupButton.setBackground(Color.decode("#1E88E5"));
		addGroupButton.setBounds(500 - 70, 10, 45, 30);
		mainPanel.add(addGroupButton);

		// Group List
		groupListModel = new DefaultListModel<>();
		fillList();

		JList<Group> groupJList = new JList<>(groupListModel);
		groupJList.setBackground(new Color(38, 38, 38));
		groupJList.setForeground(Color.WHITE);
		groupJList.setSelectionBackground(new Color(0, 149, 246, 100));
		groupJList.setSelectionForeground(Color.WHITE);
		groupJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		groupJList.setFont(new Font("Segoe UI", Font.BOLD, 14));

		JScrollPane listScrollPane = new JScrollPane(groupJList);
		listScrollPane.setBounds(10, 40, 180, 460);
		listScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
				"List of groups", 0, 0, null, Color.WHITE));
		listScrollPane.setBackground(new Color(18, 18, 18));
		mainPanel.add(listScrollPane);

		// Contact Table
		String[] colNames = { "Contact Name", "Contact City" };
		tableModel = new DefaultTableModel(colNames, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		contactTable = new JTable(tableModel);
		contactTable.setForeground(Color.WHITE);
		contactTable.setBackground(Color.decode("#1E1E1E"));
		contactTable.setGridColor(Color.DARK_GRAY);
		contactTable.setRowHeight(22);
		contactTable.setEnabled(false);

		JScrollPane tableScrollPane = new JScrollPane(contactTable);
		tableScrollPane.getViewport().setBackground(Color.decode("#1E1E1E"));
		tableScrollPane.setBounds(190, 50, 290, 450);
		tableScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		mainPanel.add(tableScrollPane);

		groupJList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					targetedGroup = groupJList.getSelectedValue();
					if (targetedGroup != null) {
						model.setTargettedGroup(targetedGroup);
						updateTable(targetedGroup);
					} else {
						targetedGroup = null;
					}
				}
			}

		});

		// Buttons
		JButton updateGroupButton = createBtn("Update");
		updateGroupButton.setBounds(270, 510, 120, 35);
		updateGroupButton.addActionListener(_ -> {
			if (this.targetedGroup != null)
				model.show("gu");
		});
		mainPanel.add(updateGroupButton);

		JButton deleteGroupButton = createBtn("Delete");
		deleteGroupButton.addActionListener(_ -> {
			if (this.targetedGroup != null) {
				int confirm = JOptionPane.showConfirmDialog(mainPanel, "are you shure , you want to delete this Group?",
						"Confirm message", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					model.deleteGroup(this.targetedGroup);
					this.targetedGroup = null;
				}
			}
		});
		deleteGroupButton.setBounds(400, 510, 70, 35);
		deleteGroupButton.setBackground(new Color(237, 73, 86));
		mainPanel.add(deleteGroupButton);

		JButton switchToContacts = createBtn("<Contacts");
		switchToContacts.setBounds(10, 510, 120, 35);
		mainPanel.add(switchToContacts);
		switchToContacts.addActionListener(_ -> {
			model.show("cdb");
		});

	}

	private void fillList() {
		groups.clear();
		groups.addAll(model.getGroups());
		groupListModel.clear();
		for (Group g : groups)
			groupListModel.addElement(g);
	}

	private JButton createBtn(String string) {
		JButton btn = new JButton(string);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBackground(new Color(38, 38, 38));
		btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btn.setBorder(BorderFactory.createEmptyBorder(4, 15, 4, 15));
		btn.setCursor(handCursor);
		return btn;
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	public void updateTable(Group targettedGroup) {
		tableModel.setRowCount(0);
		for (Contact contact : targetedGroup.getContacts()) {
			tableModel.addRow(new Object[] { contact.getFirstName() + " " + contact.getLastName(), contact.getCity() });
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		fillList();
		tableModel.setRowCount(0);
	}
}
