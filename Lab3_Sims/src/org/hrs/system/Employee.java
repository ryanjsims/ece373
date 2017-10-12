/*
 * subclass of Person, special Person that can only take one class at a time
 * added to a Course's student roster
 */

package org.hrs.system;

import java.io.*;

public class Employee extends Person implements Serializable
{
	
	private  double payRate;
	private  String id;
    
	public Employee()
	{
		super();
		payRate=0;
		id="NoID";
	}
	
	public static void saveData(Employee emp){
		try{
			FileOutputStream writeFile = new FileOutputStream("employee.ser");
			ObjectOutputStream out = new ObjectOutputStream(writeFile);
			out.writeObject(emp);
			out.close();
			writeFile.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static Employee loadData(){
		Employee toReturn = null;
		try{
			FileInputStream openFile = new FileInputStream("employee.ser");
			ObjectInputStream in = new ObjectInputStream(openFile);
			toReturn = (Employee) in.readObject();
			in.close();
			openFile.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		return toReturn;
	}
	
	protected void setPayRate(double payRate)
	{
		this.payRate=payRate;
	}
	
	protected void setID(String iD)
	{
		this.id=iD;	
	}

	
	protected double getPayRate()
	{
		return payRate;
	}
	
	protected String getID()
	{
		return id;	
	}
	
	
	
	public void raise (double percent)
	{	
		this.payRate=this.payRate*(1+ percent/100);	
	}

	
	protected double earns() 
	{
	 	double earned ;
	 	earned = 100*payRate;
	 	return earned;	
	}

	public void Print()
	{	  
	    System.out.println("\n\nName: " + this.getName());
	    System.out.println("Pay Rate ($ per hour) : " +this.getPayRate());
	    System.out.println("Employee ID : " + this.getID()); 
	    System.out.println("Employee Earns : " + this.earns());	
	}
		

}
