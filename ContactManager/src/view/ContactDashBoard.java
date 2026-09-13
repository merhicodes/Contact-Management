package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import model.Contact;
import model.Model;

public class ContactDashBoard implements Observer {
	private JPanel main;
	private Contact targetedContact;
	private DefaultListModel<Contact> listModel;
	private DefaultListModel<Contact> searchModel;
	private JList<Contact> contactsList;
	private Model model;

	public ContactDashBoard(Model model) {
		this.model = model;
		model.addObserver(this);
		new ArrayList<Contact>();
		main = new JPanel();
		main.setLayout(null);
		main.setBackground(new Color(18, 18, 18));

		// Header Title
		JLabel titleLabel = new JLabel("Contact Management");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBounds(20, 15, 250, 30);

		main.add(titleLabel);

		// Add Button (+)
		JButton addButton = new JButton("+");
		addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
		addButton.setBackground(new Color(0, 149, 246));
		addButton.setForeground(Color.WHITE);
		addButton.setBounds(420, 15, 50, 30);
		addButton.setBorder(BorderFactory.createEmptyBorder());
		addButton.setFocusable(false);
		addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		addButton.addActionListener(_ -> {
			model.show("cc");
		});
		main.add(addButton);

		// Filter DropDown (right of search field)
		JComboBox<String> filterDropdown = new JComboBox<>(new String[] { "First Name", "Last Name", "City" });
		styleDropdown(filterDropdown);
		filterDropdown.addActionListener(_ -> {
			final int sortIdx = filterDropdown.getSelectedIndex();
			List<Contact> list = Collections.list(listModel.elements());
			List<Contact> list2 = Collections.list(searchModel.elements());
			Comparator<Contact> comparator = null;
			switch (sortIdx) {
			case 0:
				comparator = new SortByFirstNameComparator();
				break;
			case 1:
				comparator = new SortByLastNameComparator();
				break;
			case 2:
				comparator = new SortByCityComparator();
				break;
			}
			Collections.sort(list, comparator);
			listModel.clear();
			listModel.addAll(list);
			Collections.sort(list2, comparator);
			searchModel.clear();
			searchModel.addAll(list2);
		});
		filterDropdown.setBounds(330, 60, 140, 35);

		main.add(filterDropdown);

		// Contacts List
		listModel = new DefaultListModel<>();
		searchModel = new DefaultListModel<>();
		updateContacts();

		contactsList = new JList<>(listModel);
		contactsList.setBackground(new Color(38, 38, 38));
		contactsList.setForeground(Color.WHITE);
		contactsList.setSelectionBackground(new Color(0, 149, 246, 100));
		contactsList.setSelectionForeground(Color.WHITE);
		contactsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contactsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					targetedContact = contactsList.getSelectedValue();
					if (targetedContact != null) {
						model.setTargettedContact(ContactDashBoard.this.targetedContact);
					}
				}
				if (e.getClickCount() == 2) {// double click
					if (targetedContact != null)
						model.show("cv");
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(contactsList);
		scrollPane.setBorder(null);
		scrollPane.setBounds(20, 110, 450, 380);
		main.add(scrollPane);

		// Search Field
		JTextField searchField = new JTextField();
		searchField.setBackground(new Color(38, 38, 38));
		searchField.setForeground(Color.WHITE);
		searchField.setCaretColor(Color.WHITE);
		searchField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(54, 54, 54)),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		searchField.setBounds(20, 60, 300, 35);

		searchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				contactsList.setModel(searchModel);
				searchField.setBackground(new Color(20, 20, 20));
			}
		});
	// remove the focus on the  search Field to shows the list Model & not the search one since it may be empty
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (searchField.getText().isEmpty()) {
					searchModel.clear();
					contactsList.setModel(listModel);
					searchField.setBackground(new Color(38, 38, 38));
				} else {
					contactsList.setModel(searchModel);
					searchField.setBackground(new Color(20, 20, 20));
				}
				filterContacts();
			}

			private void filterContacts() {
				String word = searchField.getText();
				String lowerSearch = word.toLowerCase();
				List<Contact> currentResults = new ArrayList<>();

				for (int i = 0; i < listModel.size(); i++) {
					Contact contact = listModel.getElementAt(i);
					String fullName = contact.getFirstName() + " " + contact.getLastName();// search just with firlt & last name
					if (fullName.toLowerCase().contains(lowerSearch)) {
						currentResults.add(contact);
					}
				}
				updateModelContents(searchModel, currentResults);
			}

			private void updateModelContents(DefaultListModel<Contact> model, List<Contact> newContents) {
				// Remove elements not in new contents , in case hte model was filled with in old search operations
				model.clear();
				for (int i = 0; i < newContents.size(); i++) {
					model.add(i, newContents.get(i));
				}
			}
		});

		main.add(searchField);

		// Action Buttons
		JButton updateButton = createButton("Update", 20, 510);
		updateButton.addActionListener(_ -> {
			if (targetedContact != null) {
				model.show("cu");
			}
		});
		JButton deleteButton = createButton("Delete", 150, 510);
		deleteButton.setBackground(new Color(237, 73, 86)); // Red for delete
		deleteButton.addActionListener(_ -> {
			if (targetedContact != null) {
				int confirm = JOptionPane.showConfirmDialog(main, "Do you want to delete this contact?",
						"Confirm message", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					model.deleteContact(targetedContact);
					if (!searchModel.isEmpty()) {
						searchModel.clear();
						searchField.setText("");
						contactsList.setModel(listModel);
					}

					targetedContact = null;
				}
			}
		});

		JButton groupsButton = createButton("Groups>", 600 - 250, 510);
		groupsButton.addActionListener(_ -> {
			model.show("gdb");
		});
		main.add(updateButton);
		main.add(deleteButton);
		main.add(groupsButton);
	}

	private JButton createButton(String text, int x, int y) {
		JButton button = new JButton(text);
		button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		button.setBackground(new Color(38, 38, 38));
		button.setForeground(Color.WHITE);
		button.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
		button.setBounds(x, y, 120, 35);
		button.setFocusPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return button;
	}

	private void styleDropdown(JComboBox<String> dropdown) {
		dropdown.setBackground(new Color(38, 38, 38));
		dropdown.setForeground(Color.WHITE);
		dropdown.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(54, 54, 54)),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
	}

	public JPanel getPanel() {
		return main;
	}

	private void updateContacts() {
		listModel.clear();
		listModel.addAll(model.getContacts());
		searchModel.addAll(model.getContacts());
	}

	@Override
	public void update(Observable o, Object arg) {
		updateContacts();
	}
}
