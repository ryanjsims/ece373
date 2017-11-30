package org.university.people;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.university.software.Course;

public abstract class Person implements Serializable {
	private String name;
	private ArrayList<Course> courses;
	
	public Person(){
		courses = new ArrayList<Course>();
	}
	
	private int[] getTimeSlots(){
		int[] toReturn = new int[30];
		int index = 0;
		for(int i = 100; i <= 500; i += 100){
			for(int j = 1; j <= 6; j++){
				toReturn[index] = i + j;
				index++;
			}
		}
		return toReturn;
	}
	
	public void setName(String newName){
		name = newName;
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<Course> getCourses(){
		return courses;
	}
	
	public boolean detectConflict(Course aCourse){
		for(Course course : courses){
			if(course.compareSchedules(aCourse)){
				ArrayList<String> conflicts = course.getConflictSlots(aCourse);
				for(String conflict : conflicts){
					System.out.println(aCourse.getDepartment().getDepartmentName() 
						+ aCourse.getCourseNumber() + " course cannot be added to " 
						+ name + "'s Schedule. " + aCourse.getDepartment().getDepartmentName() 
						+ aCourse.getCourseNumber() + " conflicts with " 
						+ course.getDepartment().getDepartmentName() 
						+ course.getCourseNumber() + ". Conflicting time slot is " 
						+ conflict + ".");
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean detectConflictNoPrint(Course aCourse){
		for(Course course : courses){
			if(course.compareSchedules(aCourse)){
				return true;
			}
		}
		return false;
	}
	
	public Course getConflict(Course aCourse){
		for(Course course : courses){
			if(course.compareSchedules(aCourse)){
				return course;
			}
		}
		return null;
	}
	
	public void printSchedule(){
		for(int time : getTimeSlots())
			for(Course crs : courses)
				if(crs.getMeetingTime(time) != "")
					System.out.println(crs.getMeetingTime(time) 
							+ " " + crs.getDepartment().getDepartmentName() 
							+ crs.getCourseNumber() + " " + crs.getName());
	}
	
	public void printSchedule(PrintStream stream){
		for(int time : getTimeSlots())
			for(Course crs : courses)
				if(crs.getMeetingTime(time) != "")
					stream.println(crs.getMeetingTime(time) 
							+ " " + crs.getDepartment().getDepartmentName() 
							+ crs.getCourseNumber() + " " + crs.getName());
	}
	
	public abstract void addCourse(Course aCourse);
}
