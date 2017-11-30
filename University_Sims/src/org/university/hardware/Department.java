package org.university.hardware;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.university.people.*;
import org.university.software.*;

public class Department implements Serializable {
	private String name;
	private ArrayList<Course> courses;
	private ArrayList<Student> students;
	private ArrayList<Professor> profs;
	private ArrayList<Staff> staffList;
	private HashMap<String, Staff> staffMap;
	private HashMap<String, Professor> profMap;
	private HashMap<String, Student> studentMap;
	public Department(){
		courses = new ArrayList<Course>();
		students = new ArrayList<Student>();
		profs = new ArrayList<Professor>();
		staffList = new ArrayList<Staff>();
		staffMap = new HashMap<String, Staff>();
		profMap = new HashMap<String, Professor>();
		studentMap = new HashMap<String, Student>();
	}
	
	public Staff getStaff(String name){
		if(staffMap.containsKey(name))
			return staffMap.get(name);
		return null;
	}
	
	public Professor getProfessor(String name){
		if(profMap.containsKey(name))
			return profMap.get(name);
		return null;
	}
	
	public Student getStudent(String name){
		if(studentMap.containsKey(name))
			return studentMap.get(name);
		return null;
	}
	
	public void setDepartmentName(String name){
		this.name = name;
	}

	public String getDepartmentName(){
		return name;
	}

	public void addStaff(Staff stf){
		staffList.add(stf);
		staffMap.put(stf.getName(), stf);
		if(stf.getDepartment() == null || stf.getDepartment() != this){
			stf.setDepartment(this);
		}
	}

	public ArrayList<Staff> getStaffList(){
		return staffList;
	}

	public void addProfessor(Professor prof){
		profs.add(prof);
		profMap.put(prof.getName(), prof);
		if(prof.getDepartment() == null || prof.getDepartment() != this){
			prof.setDepartment(this);
		}
	}
	
	public ArrayList<Professor> getProfessorList(){
		return profs;
	}

	public void addStudent(Student student){
		students.add(student);
		studentMap.put(student.getName(), student);
		if(!student.getDepartment().equals(this))
			student.setDepartment(this);
	}
	
	public ArrayList<Student> getStudentList(){
		return students;
	}

	public void addCourse(Course course){
		courses.add(course);
		if(!course.getDepartment().equals(this))
			course.setDepartment(this);
	}
	
	public ArrayList<Course> getCourseList(){
		return courses;
	}
	
	public Course getCourse(int crsNum){
		for(Course crs : courses){
			if(crs.getCourseNumber() == crsNum)
				return crs;
		}
		return null;
	}

	public void printStudentList(){
		for(Student s : students){
			System.out.println(s.getName());
		}
	}
	
	public void printStudentList(PrintStream stream){
		for(Student s : students){
			stream.println(s.getName());
		}
	}
	
	public void printProfessorList(){
		for(Professor s : profs){
			System.out.println(s.getName());
		}
	}
	
	public void printProfessorList(PrintStream stream){
		for(Professor s : profs){
			stream.println(s.getName());
		}
	}

	public void printStaffList(){
		for(Staff s : staffList){
			System.out.println(s.getName());
		}
	}
	
	public void printStaffList(PrintStream stream){
		for(Staff s : staffList){
			stream.println(s.getName());
		}
	}
	
	public void printCourseList(){
		for(Course s : courses){
			System.out.println(name + s.getCourseNumber());
		}
	}
	
	public void printCourseList(PrintStream stream){
		for(Course s : courses){
			stream.println(name + s.getCourseNumber());
		}
	}
}
