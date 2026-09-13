package model;

import java.util.ArrayList;

public class Contact {
	private int id;
	private String firstName;
	private String lastName;
	private String city;
	private ArrayList<String> numbers;

	public Contact(int id, String fname, String lname, String city, ArrayList<String> numbers2) {
		this.id = id;
		this.firstName = fname;
		lastName = lname;
		this.city = city;
		numbers = new ArrayList<>(numbers2);
	}

	public Contact(String text, String text2, String text3, ArrayList<String> numbers) {
		this(-1, text, text2, text3, numbers);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Contact) {
			Contact c = (Contact) o;
			return c.getId() == this.id;
		}
		return false;
	}

	public ArrayList<String> getNumbers() {
		return new ArrayList<>(numbers);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getId() {
		return id;
	}

	void addNumber(String n) {
		numbers.add(n);
	}

	void remove(String n) {
		numbers.remove(n);
	}

	public String toString() {
		return firstName + " " + lastName + " live in " + city;
	}

	public void clear() {
		numbers.clear();
	}

	public void setNumbers(ArrayList<String> numbers) {
		this.numbers = numbers;
	}

	public void addToGroups(ArrayList<Group> groups, ArrayList<Group> groups2) {
		for (Group g : groups) {
			if (groups2.contains(g)) {// selected it now
				if (!g.contains(this)) {// & it wasn't in before
					g.addContanct(this);
				}
			} else {// unselected it now
				if (g.contains(this)) {// it was before
					g.remove(this);
				}
			}
		}
	}

	public void setId(int id) {
		this.id = id;
	}

}
