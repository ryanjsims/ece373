package org.university.software;

import java.util.ArrayList;

import org.university.hardware.*;

public class University {
	public ArrayList<Department> departmentList;
	public ArrayList<Classroom> classroomList;
	
	public University(){
		departmentList = new ArrayList<Department>();
		classroomList = new ArrayList<Classroom>();
	}
	
	public void printStudentList(){
		for(Department dept : departmentList){
			dept.printStudentList();
		}
	}
	
	public void printStaffList(){
		for(Department dept : departmentList){
			dept.printStaffList();
		}
	}
	
	public void printProfessorList(){
		for(Department dept : departmentList){
			dept.printProfessorList();
		}
	}
	
	public void printCourseList(){
		for(Department dept : departmentList){
			dept.printCourseList();
		}
	}
}
