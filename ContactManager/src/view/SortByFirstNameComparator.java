package view;

import java.util.Comparator;

import model.Contact;

public class SortByFirstNameComparator implements Comparator<Contact> {
	@Override
	public int compare(Contact o1, Contact o2) {
		return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
	}

}
