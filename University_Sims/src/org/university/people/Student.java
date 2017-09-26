package org.university.people;
import org.university.hardware.*;
import org.university.software.*;

public class Student extends Person {
	
	private Department dept;
	private int unitsCompleted;
	private int totalUnitsNeeded;
	
	public Student(){
		dept = new Department();
		unitsCompleted = 0;
		totalUnitsNeeded = 0;
	}
	
	public void setCompletedUnits(int units){
		unitsCompleted = units;
	}
	
	public int getUnitsCompleted(){
		return unitsCompleted;
	}
	
	public int requiredToGraduate(){
		return totalUnitsNeeded - unitsCompleted;
	}
	
	public void setRequiredCredits(int units){
		totalUnitsNeeded = units;
	}
	
	public int getTotalUnits(){
		return totalUnitsNeeded;
	}
		
	public void setDepartment(Department dept){
		this.dept = dept;
		if(!this.dept.getStudentList().contains(this)){
			this.dept.addStudent(this);
		}
	}
	
	public Department getDepartment(){
		return dept;
	}
	
	public void addCourse(Course crs){
		if(!detectConflict(crs)){
			super.getCourses().add(crs);
			if(!crs.getStudentRoster().contains(this))
				crs.addStudent(this);
		}
	}
	
	public void dropCourse(Course crs){
		if(super.getCourses().contains(crs)){
			super.getCourses().remove(crs);
			if(crs.getStudentRoster().contains(this))
				crs.removeStudent(this);
		}
		else{
			System.out.println("The course " + crs.getDepartment().getDepartmentName() 
					+ crs.getCourseNumber() + " could not be "
					+ "dropped because " + super.getName() + " is not enrolled in " 
					+ crs.getDepartment().getDepartmentName() 
					+ crs.getCourseNumber() + ".");
		}
	}
	
}
