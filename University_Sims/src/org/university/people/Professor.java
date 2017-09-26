package org.university.people;

import org.university.hardware.Department;
import org.university.software.Course;

public class Professor extends Employee {
	private Department dept;
	
	public Professor(){
		dept = null;
	}
	
	public void setDepartment(Department newDept){
		dept = newDept;
	}
	
	public Department getDepartment(){
		return dept;
	}
	
	@Override
	public double earns() {
		return 200 * super.getPayRate();
	}

	@Override
	public void addCourse(Course aCourse) {
		if(aCourse.getProfessor() != null){
			System.out.println("The professor cannot be assigned to this course"
					+ " because professor " + aCourse.getProfessor().getName() 
					+ " is already assigned to the course " + aCourse.getName() + ".");
		}
		else if(!detectConflict(aCourse)){
			aCourse.setProfessor(this);
			super.getCourses().add(aCourse);
		}
	}
	
}
