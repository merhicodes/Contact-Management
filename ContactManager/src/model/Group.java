package model;

import java.util.ArrayList;

public class Group implements Comparable<Group> {
	private int id;
	private String name;
	private String description;
	private ArrayList<Contact> contacts;

	public Group(int id, String name, String desc, ArrayList<Contact> contacts) {
		this.id = id;
		this.name = name;
		this.description = desc;
		this.contacts = new ArrayList<>(contacts);
	}

	public int getCountOfContacts() {
		return contacts.size();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void addContanct(Contact c) {
		if (!contacts.contains(c)) {
			contacts.add(c);
		}
	}

	public void remove(Contact c) {
		if (contacts.contains(c)) {
			contacts.remove(c);
		}
	}

	@Override
	public int compareTo(Group o) {
		return id - o.getId();
	}

	@Override
	public boolean equals(Object o) {
		Group g = (Group) o;
		return this.id == g.getId();
	}

	public boolean contains(Contact c) {
		return contacts.contains(c);
	}

	public ArrayList<Contact> getContacts() {
		ArrayList<Contact> res = new ArrayList<>();// read only without editing on the original contacts
		res.addAll(contacts);
		return res;
	}

	public void setName(String text) {
		this.name = text;
	}

	public void setDescription(String text) {
		this.description = text;
	}

	@Override
	public String toString() {
		return name + " [" + contacts.size() + "]";
	}

	public void replaceContats(ArrayList<Contact> contacts2) {
		contacts.clear();
		contacts.addAll(contacts2);
	}

	public void setId(int id) {
		this.id = id;
	}

}
