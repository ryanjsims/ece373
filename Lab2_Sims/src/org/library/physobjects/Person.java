package org.library.physobjects;

import java.util.ArrayList;





public class Person {

	//Fields for a Person
	private String name;
	private int maxBooks;
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
	
	
	public void borrowBook(Book b1) {
		
        boolean flag=false;
        
		if(b1.getPerson()==null) { 
	 
		 flag = checkBookLimit() ;
		 
			if (flag ) {
			   bookList.add(b1);
		       b1.setPerson(this);
			}	
		   else {  	
			   System.out.println(b1.getTitle() + " cannot be lent to " + this.getName()+ ". Max limit of books is " + getMaxBooks());		
		   }
		}	
    	else {
		System.out.println( b1.getTitle() + " cannot be lent to " + this.getName()+ ". Already lent to " + b1.getPerson().getName());
		
	   }
 } 		
		
		
		
		public void returnBook(Book b1) {

	 		boolean flag=false;
			
			for ( Book aBook:this.getBookList()) {
			  
	            	if(aBook.getTitle()==b1.getTitle() && aBook.getAuthor()==b1.getAuthor()){ 
	          		
	          		flag=true;
	          	   
					}
					
				
			}
			
			if(flag){
				  		b1.setPerson(null);
		          		bookList.remove(b1);
		          		
			}
	     
			
		}
 
	
}
