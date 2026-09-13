package view;

import java.util.Comparator;

import model.Contact;

class SortByCityComparator implements Comparator<Contact> {

	@Override
	public int compare(Contact o1, Contact o2) {
		return o1.getCity().compareToIgnoreCase(o2.getCity());
	}

}
