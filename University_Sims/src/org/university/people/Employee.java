package org.university.people;

import java.io.Serializable;

public abstract class Employee extends Person {
	private double payRate;

	public Employee(){
		payRate = 0.0;
	}

	public void setPayRate(double pay){
		payRate = pay;
	}

	public double getPayRate(){
		return payRate;
	}

	public void raise(double percent){
		payRate += (percent / 100.0) * payRate;
	}

	public abstract double earns();
	
}
