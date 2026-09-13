package view;

import java.util.Comparator;

import model.Contact;

class SortByLastNameComparator implements Comparator<Contact> {
	@Override
	public int compare(Contact o1, Contact o2) {
		return o1.getLastName().compareToIgnoreCase(o2.getLastName());
	}
}
