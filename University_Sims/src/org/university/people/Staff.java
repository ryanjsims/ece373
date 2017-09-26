package org.university.people;

import org.university.hardware.Department;
import org.university.software.Course;

public class Staff extends Employee {
	private Department dept;
	private double hoursWorked;
	
	public Staff(){
		hoursWorked = 0.0;
		dept = null;
	}
	
	public void setMonthlyHours(double hours){
		hoursWorked = hours;
	}
	
	public double getMonthlyHours(){
		return hoursWorked;
	}
	
	public void setDepartment(Department dept){
		this.dept = dept;
	}
	
	public Department getDepartment(){
		return dept;
	}
	
	@Override
	public double earns() {
		return hoursWorked * super.getPayRate();
	}

	@Override
	public void addCourse(Course aCourse) {
		if(super.getCourses().size() > 0){
			System.out.println(super.getCourses().get(0).getDepartment().getDepartmentName() 
					+ super.getCourses().get(0).getCourseNumber() + " is removed from "
					+ super.getName() + "'s schedule(Staff can only take one class at a time). "
					+ aCourse.getDepartment().getDepartmentName() + aCourse.getCourseNumber()
					+ " has been added to " + super.getName() + "'s Schedule.");
			super.getCourses().get(0).removeStudent(this);
			super.getCourses().remove(0);
		}
		super.getCourses().add(aCourse);
		aCourse.addStudent(this);
	}

}
