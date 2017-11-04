package org.university.hardware;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.university.software.Course;

public class Classroom implements Serializable {
	
	private ArrayList<Course> courses;
	private String room;
	
	public Classroom(){
		courses = new ArrayList<Course>();
		room = null;
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
	
	public void setRoomNumber(String newRoom){
		room = newRoom;
	}
	
	public String getRoomNumber(){
		return room;
	}
	
	public void addCourse(Course aCourse){
		boolean flag = false;
		for(Course crs : courses){
			if(crs.compareSchedules(aCourse)){
				System.out.println(aCourse.getDepartment().getDepartmentName() 
						+ aCourse.getCourseNumber() + " conflicts with " 
						+ crs.getDepartment().getDepartmentName() + crs.getCourseNumber()
						+ ". Conflicting time slot " + aCourse.getConflictSlots(crs).get(0)
						+ ". " + aCourse.getDepartment().getDepartmentName() 
						+ aCourse.getCourseNumber() + " course cannot be added to " 
						+ room + "'s Schedule.");
				flag = true;
				break;
			}
		}
		if(!flag){
			courses.add(aCourse);
		}
	}
	
	public void printSchedule(){
		int[] timeSlots = getTimeSlots();
		for(int time : timeSlots){
			String slot = "";
			String className = "";
			for(Course crs : courses){
				if(crs.getMeetingTime(time) != ""){
					slot = crs.getMeetingTime(time);
					className = crs.getDepartment().getDepartmentName() 
							+ crs.getCourseNumber() + " " + crs.getName();
					break;
				}
			}
			if(slot != "")
				System.out.println(slot + " " + className);
		}
	}
	
	public void printSchedule(PrintStream stream){
		int[] timeSlots = getTimeSlots();
		for(int time : timeSlots){
			String slot = "";
			String className = "";
			for(Course crs : courses){
				if(crs.getMeetingTime(time) != ""){
					slot = crs.getMeetingTime(time);
					className = crs.getDepartment().getDepartmentName() 
							+ crs.getCourseNumber() + " " + crs.getName();
					break;
				}
			}
			if(slot != "")
				stream.println(slot + " " + className);
		}
	}
}
