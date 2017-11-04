package org.library.physobjects;

/*
 * To get this class to work with MyLibraryTest,
 * maxBooks was made public. 
 */

public class Professor extends Employee{
	int accessCode;
	public Professor(){
		accessCode = 0;
	}
	
	public void increaseMaxLimit(int limit){
		maxBooks = limit;
		System.out.println(getName() + "'s Max limit is now " + maxBooks);
	}
	
	public int getAccessCode(){
		return accessCode;
	}
	
	public void setAccessCode(int newCode){
		accessCode = newCode;
	}
}
