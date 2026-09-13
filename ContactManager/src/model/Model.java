package model;

import java.awt.CardLayout;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import view.ContactCreate;
import view.ContactDashBoard;
import view.ContactUpdate;
import view.ContactView;
import view.GroupCreate;
import view.GroupDashBoard;
import view.GroupUpdate;

public class Model extends Observable {

	private ContactDashBoard cdb;
	private ContactView cv;
	private ContactUpdate cu;
	private ContactCreate cc;
	private GroupDashBoard gdb;
	private GroupUpdate gu;
	private GroupCreate gc;
	private JPanel mainPanel;
	private CardLayout cl;

	private ArrayList<Contact> contacts;
	private ArrayList<Group> groups;
	private final File contactFile, groupFile;

	public Model(JPanel mainPanel) {
		contacts = new ArrayList<>();
		groups = new ArrayList<>();
		contactFile = new File("src//model//contacts.csv");
		groupFile = new File("src//model//groups.csv");
		load();

		cdb = new ContactDashBoard(this);
		cv = new ContactView("View Contact", this);
		cu = new ContactUpdate(this);
		cc = new ContactCreate(this);
		// group panels
		gdb = new GroupDashBoard(this);
		gu = new GroupUpdate(this);
		gc = new GroupCreate("Create Group", this);
		this.mainPanel = mainPanel;
		setMainPanel();
	}

	public void createContact(String fname, String lname, String city, DefaultTableModel phones,
			ArrayList<Group> groups2) {
		Contact contact = new Contact(fname, lname, city, createListFrom(phones));
		contact.addToGroups(getGroups(), groups2);
		addContact(contact);
	}

	public void setTargettedGroup(Group targetedGroup) {
		gu.setTargetedGroup(targetedGroup);
	}

	public void show(String panelName) {
		if (panelName == "cu") {
			cu.setEnabled();// makes fields enabled before going there
		} else if (panelName == "cc") {
			cc.setEnabled();
		}
		cl.show(mainPanel, panelName);
	}

	public void setTargettedContact(Contact c) {
		cu.setTargetedContact(c);
		cv.setTargetedContact(c);
	}

	private void setMainPanel() {
		cl = (CardLayout) mainPanel.getLayout();
		mainPanel.add(cdb.getPanel(), "cdb");
		mainPanel.add(cv.getPanel(), "cv");
		mainPanel.add(cu.getPanel(), "cu");
		mainPanel.add(cc.getPanel(), "cc");
		mainPanel.add(gdb.getPanel(), "gdb");
		mainPanel.add(gu.getPanel(), "gu");
		mainPanel.add(gc.getPanel(), "gc");
		cl.show(mainPanel, "cdb");
	}

	public void load() {
		try {
			loadContacts();// contacts should be loaded before groups !
			loadGroups();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	private void loadGroups() throws IOException {
		Reader fr = new FileReader(groupFile);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();// skip the head
		while ((line = br.readLine()) != null) {
			String[] data = line.split(",");
			if (data.length >= 5) {// has necessary data
				int id = Integer.parseInt(data[0]);
				String name = data[1];
				String desc = data[2];
				ArrayList<Contact> groupContacts = new ArrayList<>();
				int i = 3;
				while (i < data.length) {
					int contactId = Integer.parseInt(data[i]);
					Contact contact = getContactsOfId(contactId);
					if (contact != null)
						groupContacts.add(contact);
					i++;
				}
				Group group = new Group(id, name, desc, groupContacts);
				groups.add(group);
			}
		}
		fr.close();
		br.close();
	}

	private Contact getContactsOfId(int contactId) {
		Contact c = null;
		for (Contact cont : contacts) {
			if (cont.getId() == contactId)
				return cont;
		}
		return c;
	}

	private void loadContacts() throws IOException {
		BufferedReader br =  new BufferedReader(
				new InputStreamReader(new FileInputStream(contactFile), StandardCharsets.UTF_8));
		String line = br.readLine();// skip the head
		while ((line = br.readLine()) != null) {
			String[] data = line.split(",");
			if (data.length >= 5) {// has necessary data
				int id = Integer.parseInt(data[0]);
				String fname = data[1];
				String lname = data[2];
				String city = data[3];
				//reading phone numbers
				ArrayList<String> numbers = new ArrayList<>();
				int i = 4;
				while (i < data.length) {
					numbers.add(data[i]);
					i++;
				}
				Contact contact = new Contact(id, fname, lname, city, numbers);
				contacts.add(contact);
			}
		}
		br.close();
	}

	public void save() {
		try {
			saveContacts();
			saveGroups();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveContacts() throws IOException {
		Writer fw = new FileWriter(contactFile);
		BufferedWriter bw = new BufferedWriter(fw);
		String head = "id,fname,lname,city,numbers";
		bw.write(head);// do not write
		for (Contact contact : contacts) {
			String line = contact.getId() + "," + contact.getFirstName() + "," + contact.getLastName() + ","
					+ contact.getCity();
			for (String num : contact.getNumbers()) {
				line += "," + num;
			}
			line = "\n" + line;
			bw.write(line.toCharArray(), 0, line.length());
		}
		bw.close();
	}

	private void saveGroups() throws IOException {
		Writer fw = new FileWriter(groupFile);
		BufferedWriter bw = new BufferedWriter(fw);
		String head = "id,name,description,subscribers";
		bw.write(head);
		for (Group group : groups) {
			String line = group.getId() + "," + group.getName() + "," + group.getDescription();
			for (Contact contact : group.getContacts()) {
				line += "," + contact.getId();
			}
			line = "\n" + line;
			bw.write(line.toCharArray(), 0, line.length());
		}
		bw.close();
	}

	public ArrayList<Group> getGroups() {
		return groups;
	}

	public ArrayList<Contact> getContacts() {
		return contacts;
	}

	public void deleteContact(Contact c) {
		if (contacts.contains(c)) {
			removeContactFromGroups(c);
			contacts.remove(c);
			this.setChanged();
			this.notifyObservers();
		}
	}

	private void removeContactFromGroups(Contact c) {
		Iterator<Group> it = groups.iterator();
		while (it.hasNext()) {
			Group g = it.next();
			g.remove(c);
		}
	}

	public ArrayList<Group> getGroupsOf(Contact contact) {
		ArrayList<Group> res = new ArrayList<>();
		for (Group g : groups) {
			if (g.contains(contact))
				res.add(g);
		}
		return res;
	}

	public Group getGroupOf(int id) {
		for (Group g : groups) {
			if (g.getId() == id)
				return g;
		}
		return null;
	}

	public void updatetargettedcontact(String fname, String lname, String city, DefaultTableModel phones,
			ArrayList<Group> groups2, Contact targettedContact) {
		Contact c = targettedContact;
		c.setFirstName(fname);
		c.setLastName(lname);
		c.setCity(city);
		// update numbers
		ArrayList<String> numbers = createListFrom(phones);
		c.setNumbers(numbers);
		// update groups
		c.addToGroups(groups, groups2);
		this.setChanged();
		this.notifyObservers();
	}

	public ArrayList<String> createListFrom(DefaultTableModel phones) {
		ArrayList<String> numbers = new ArrayList<>();
		for (int i = 0; i < phones.getRowCount(); i++) {
			Object regionObj = phones.getValueAt(i, 0);
			Object numObj = phones.getValueAt(i, 1);
			if (regionObj != null && numObj != null) {
				String region = regionObj.toString();
				String num = numObj.toString();
				if (!num.isEmpty() && !region.isEmpty())
					numbers.add(region + num);
			}
		}
		return numbers;
	}

	public void addContact(Contact contact) {
		if (!contacts.contains(contact)) {
			contact.setId(getMaxContactId() + 1);
			contacts.add(contact);
			this.setChanged();
			this.notifyObservers();
		}

	}

	private int getMaxContactId() {
		int id = -1;
		for (Contact c : contacts)
			if (id < c.getId())
				id = c.getId();
		return id;
	}

	public Contact getContactOf(int id) {
		for (Contact c : contacts) {
			if (c.getId() == id)
				return c;
		}
		return null;
	}

	public void addGroup(Group g) {
		if (g != null) {
			g.setId(getMaxGroupId() + 1);
			groups.add(g);
			this.setChanged();
			this.notifyObservers();
		}
	}

	private int getMaxGroupId() {
		int id = -1;
		for (Group g : groups)
			if (id < g.getId()) {
				id = g.getId();
			}
		return id;
	}

	public void setGroup(Group targetedGroup, Group g) {
		Iterator<Group> it = groups.iterator();
		while (it.hasNext()) {
			Group cur = it.next();
			if (cur.getId() == targetedGroup.getId()) {
				cur.setDescription(g.getDescription());
				cur.setName(g.getName());
				cur.replaceContats(g.getContacts());
				this.setChanged();
				this.notifyObservers();
				break;
			}
		}
	}

	public void deleteGroup(Group group) {
		groups.remove(group);
		group = null;// isn't saved in the targetedGroup so can't be updated
		this.setChanged();
		this.notifyObservers();
	}
}
