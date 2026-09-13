# Contact-Management
# Contact Management Project

## Overview
A desktop application built using the Model-View-Controller (MVC) and Observer design patterns to manage personal contacts, groups, and their interrelationships. Application state and persistence logic are managed centrally.

---

## Features
* **Contact Management**: Create, update, view, and remove individual contacts
* **Group Management**: Create, update, view, and remove contact groups.
* **Bidirectional Relations**: Manage relationships between groups and contacts from either side.

---

## Architecture & Design Patterns
* **Pattern**: MVC / Observer pattern implementation ensuring UI interfaces update automatically upon data changes.
* **Layout**: Main panel managed using `CardLayout`.
* **State & Persistence**: Handled via `ContactManagementApp`, saving/loading data to/from disk files (`contactFile`, `groupfile`).

---

## Core Domain Classes

### `Contact`
* **Description**: Represents an individual contact with phone numbers.
* **Attributes**: `id` (int), `firstName` (String), `lastName` (String), `city` (String), `numbers` (ArrayList<String>) .
* **Key Methods**: `equals()`, `addNumber()`, `toString()`, `clear()`, `addToGroups()`.

### `Group`
* **Description**: Contains multiple contacts with a description.
* **Attributes**: `id` (int), `name` (String), `description` (String), `contacts` (ArrayList<Contact>).
* **Key Methods**: `getCountOfContacts()`, `addContact()`, `remove()`, `replaceContact()`.

---

## UI Components & Dashboards
* **Dashboards**: 
  * `ContactDashBoard` (manages contact lists, search models, and targeted contacts) .
  * `GroupDashBoard` (manages group lists, contact tables, and targeted groups).
* **Views / Forms**: Dedicated creation and update views for contacts and groups (`ContactView`, `ContactCreate`, `ContactUpdate`, `GroupCreate`, `GroupUpdate`).

---

## Persistence Operations (`Model`)
* `load()` / `save()`: Load and save contacts and groups array lists to/from files.
* `createContact()` / `updatetargettedcontact()` / `deleteContact()`.
* `addGroup()` / `setGroup()` / `deleteGroup()`.
* Relationship helpers: `removeContactFromGroups()`, `getGroupsOf()`, `getGroupOf()`.

---

## Skills Improved
* **MVC**: Improving skills in creating MVC models to update concerned interfaces after any changes in data.
* **OOP**: Improving object-oriented programming mindset and specifying relationships between objects.
* **Design**: Simulating famous program designs to improve overall design taste.
* **Personal Growth**: Facing problems and solving them before a deadline builds patience and resilience.

---

## Future Roadmap / Potential Features
* Relate the application to a database (DB).
* Add messaging and calling features to explore networking and APIs.

---

## Developer Note
> **Apologies**: Code may feel overlapping or slightly disorganized due to multiple structural redesigns and iterative updates.

<img width="640" height="674" alt="Screenshot from 2026-09-13 19-20-22" src="https://github.com/user-attachments/assets/85227fa6-6f81-48ea-aaa7-f8032028eb1f" />

<img width="640" height="674" alt="Screenshot from 2026-09-13 19-20-33" src="https://github.com/user-attachments/assets/504fd142-ec7c-4194-8985-7708bf22c1f4" />


