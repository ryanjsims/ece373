package org.university.hardware;
import java.util.ArrayList;
import org.university.people.*;
import org.university.software.*;

public class Department {
	private String name;
	private ArrayList<Course> courses;
	private ArrayList<Student> students;
	private ArrayList<Professor> profs;
	private ArrayList<Staff> staffList;
	public Department(){
		courses = new ArrayList<Course>();
		students = new ArrayList<Student>();
		profs = new ArrayList<Professor>();
		staffList = new ArrayList<Staff>();
	}
	
	public void setDepartmentName(String name){
		this.name = name;
	}

	public String getDepartmentName(){
		return name;
	}

	public void addStaff(Staff stf){
		staffList.add(stf);
		if(stf.getDepartment() == null || stf.getDepartment() != this){
			stf.setDepartment(this);
		}
	}

	public ArrayList<Staff> getStaffList(){
		return staffList;
	}

	public void addProfessor(Professor prof){
		profs.add(prof);
		if(prof.getDepartment() == null || prof.getDepartment() != this){
			prof.setDepartment(this);
		}
	}
	
	public ArrayList<Professor> getProfessorList(){
		return profs;
	}

	public void addStudent(Student student){
		students.add(student);
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

	public void printStudentList(){
		for(Student s : students){
			System.out.println(s.getName());
		}
	}
	
	public void printProfessorList(){
		for(Professor s : profs){
			System.out.println(s.getName());
		}
	}

	public void printStaffList(){
		for(Staff s : staffList){
			System.out.println(s.getName());
		}
	}
	
	public void printCourseList(){
		for(Course s : courses){
			System.out.println(name + s.getCourseNumber());
		}
	}
}
