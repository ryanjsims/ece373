package org.library.system;

import java.util.ArrayList;

import org.library.physobjects.Book;

import org.library.physobjects.Person;



public class MyLibrary {

	String name;
	ArrayList<Book> books;
	ArrayList<Person> people;

	public MyLibrary() {
		name = "Default Library";
		books = new ArrayList<Book>();
		people = new ArrayList<Person>();
		
	}
    
    public void setName(String name){
        this.name = name;
    }

	public String getName() {
		return name;
	}

	public ArrayList<Book> getBooks() {
		return books;
	}

	public ArrayList<Person> getPeople() {
		return people;
	}

	public void addBook(Book b1) {
		books.add(b1);
		
	}

	public void removeBook(Book b1) {
		this.books.remove(b1);
		
	}
	
	public void addPerson(Person p1) {
		this.people.add(p1);
	}
	
	public void removePerson(Person p1) {
		this.people.remove(p1);
	}



	

	/**
	 * This method returns a list of books not currently checked out. If all
	 * books are checked out, an empty list is returned.
	 * 
	 * @return List of books not checked out.
	 */
	public ArrayList<Book> getAvailableBooks() {
		ArrayList<Book> result = new ArrayList<Book>();
		for (Book book : this.getBooks()) {
			if (book.getPerson() == null) {
				result.add(book);
			}
		}
		return result;
	}

	

	/**
	 * This method prints information about this
	 * MyLibrary object to the system console.
	 */
	private void printStatus() {
		
		System.out.println("\nLatest Status Report of " + this.getName() + " ....... \n" + 
				this.toString());

		for (Book thisBook : this.getBooks()) {
			System.out.println(thisBook);
		}
		
		for (Person thisPerson : this.getPeople()) {
			//int count = this.getBooksForPerson(thisPerson).size();
			System.out.println(thisPerson.getName() + " has " + thisPerson.getBookList().size()  + 
					" of  Maximum limit " + thisPerson.getMaxBooks());
		}
		
		System.out.println("Total Books Available to Lend: "
				+ this.getAvailableBooks().size());
		System.out.println("\n--- End of Status Report---\n");		
	}
	
	@Override
	public String toString() {
		return this.getName() + 
		" has " + this.getBooks().size() + " books and " 
		+ this.getPeople().size() + " people.";
	}
	

	
	public static void main(String[] args) {
		// create a new mylibrary
	
		MyLibrary testLibrary = new MyLibrary();
        testLibrary.setName("My Library");
		Book b1 = new Book();
		b1.setTitle("Romeo and Juliet");
		b1.setAuthor("Shakespear");
		Book b2 = new Book();
		b2.setTitle("Tale of Two Cities");
		b2.setAuthor("Charles Dickens");
		Book b3 = new Book();
		b3.setTitle("Mother");
		b3.setAuthor("Maxim Gorky");
		Book b4 = new Book();
		b4.setTitle("My Times");
		b4.setAuthor("Bill Clinton");
		
		Person  jack = new Person();
		jack.setName("Jack Benny");
		Person   lahiru =  new Person();
	
		lahiru.setName("Lahiru");
	
	
		testLibrary.addBook(b1);
		testLibrary.addBook(b2);
		testLibrary.addBook(b3);
		testLibrary.addBook(b4);
		
		testLibrary.addPerson(jack);
		testLibrary.addPerson(lahiru);
		
		System.out.println("------New Library Created --------\n");
		testLibrary.printStatus();

		
		lahiru.borrowBook(b1);
		lahiru.borrowBook(b2);
		lahiru.borrowBook(b3);
		jack.borrowBook(b4);
		
		testLibrary.printStatus();
		
		
		jack.borrowBook(b3);
		lahiru.borrowBook(b3);
		

		testLibrary.printStatus();
		
		jack.returnBook(b3);
				
		testLibrary.printStatus();
		
	}

}

