package view;

import model.Group;
import model.Model;

public class GroupUpdate extends GroupCreate {

	private Group targetedGroup;

	public GroupUpdate(Model model) {
		super("Update Group", model);
	}

	public void setTargetedGroup(Group g) {
		this.targetedGroup = g;
		setFields();
	}

	protected void setFields() {
		if (this.targetedGroup != null) {
			super.groupNameField.setText(targetedGroup.getName());
			super.descField.setText(targetedGroup.getDescription());
			myContacts.clear();
			myContacts.addAll(targetedGroup.getContacts());
			loadContacts();
		}
	}

	public Group getTargetedGroup() {
		return this.targetedGroup;
	}

}
