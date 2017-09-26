package org.library.physobjects;

import java.util.ArrayList;





public abstract class Person {

	//Fields for a Person
	private String name;
	public int maxBooks;
	private ArrayList<Book> bookList; 
	
	
	//Constructor
	public Person() {
		name = "unKnown";
		maxBooks = 2;
		bookList = new ArrayList<Book>();
	}
	
	
	//getters and setters
	
	public ArrayList<Book>  getBookList(){
		return bookList;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String aName){
		name = aName;
	}
	
	public int getMaxBooks() {
		return maxBooks;
	}

	// methods
	
	
	public boolean checkBookLimit() {
		
		boolean flag=false;
		
	    int currSize=this.getBookList().size();
	     
	   
			if (  currSize < maxBooks) {
		     flag=true;	
		    }	
		   
		return flag;	
	
	}
	
	
	public abstract void borrowBook(Book b1);
	
	public abstract void returnBook(Book b1);
 
	
}
