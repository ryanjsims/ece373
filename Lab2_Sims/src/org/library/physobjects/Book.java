package org.library.physobjects;


public class Book {


	private String title;
	private String author;
	private Person person;

	public Book() {
		this.title = "unknown title";
		this.author = "unknown author";
		person = null; 
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String bookName) {
		this.title = bookName;
	}
	

	public void setPerson(Person p2) {
		this.person = p2;
	}

	public Person getPerson() {
		return this.person;
	}
	
	@Override
	
	
	
	

	public String toString() {
		String available;
	
		if (this.getPerson() == null) {
			available = "Available";
		}
		else {
			available = "Checked out to " + 
			this.getPerson().getName();
		}
		return this.getTitle() + 
		" by " + this.getAuthor() +
		": " + available;
	}
}
