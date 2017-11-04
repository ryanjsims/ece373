package org.university.software;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.university.hardware.*;
import org.university.people.Professor;
import org.university.people.Staff;
import org.university.people.Student;

public class University implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4385380273941983550L;
	public ArrayList<Department> departmentList;
	public ArrayList<Classroom> classroomList;
	
	public University(){
		departmentList = new ArrayList<Department>();
		classroomList = new ArrayList<Classroom>();
	}
	
	public static void saveData(University univ){
		try{
			FileOutputStream writeFile = new FileOutputStream("university.ser");
			ObjectOutputStream out = new ObjectOutputStream(writeFile);
			out.writeObject(univ);
			out.close();
			writeFile.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static University loadData(){
		University toReturn = null;
		try{
			FileInputStream openFile = new FileInputStream("university.ser");
			ObjectInputStream in = new ObjectInputStream(openFile);
			toReturn = (University) in.readObject();
			in.close();
			openFile.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		return toReturn;
	}
	
	public void printAll(){
		
		System.out.println("\nList of departments:");
		printDeptList();
		System.out.println();
		
		System.out.println("Classroom list:");
		printClassroomList();
		
		for(Department dept : departmentList){
			System.out.println("\nThe professor list for department " 
								+ dept.getDepartmentName());
			dept.printProfessorList();
		}
		
		for(Department dept : departmentList){
			System.out.println("\nThe course list for department " 
								+ dept.getDepartmentName());
			dept.printCourseList();
		}
		
		for(Classroom room : classroomList){
			System.out.println("\nThe schedule for classroom " + room.getRoomNumber());
			room.printSchedule();
		}
		
		for(Department dept : departmentList){
			
			System.out.println("\nDepartment " + dept.getDepartmentName());
			System.out.println();
			
			System.out.println("Printing Professor schedules:");
			System.out.println();
			for(Professor prof : dept.getProfessorList()){
				System.out.println("The schedule for Prof. " + prof.getName() + ":");
				prof.printSchedule();
				System.out.println();
			}
			
			System.out.println("Printing Student Schedules:");
			System.out.println();
			for(Student stu : dept.getStudentList()){
				System.out.println("The schedule for Student " + stu.getName() + ":");
				stu.printSchedule();
				System.out.println();
			}
			
			System.out.println("Printing Staff Schedules:");
			System.out.println();
			for(Staff staff : dept.getStaffList()){
				System.out.println("The schedule for Employee " + staff.getName() + ":");
				staff.printSchedule();
				System.out.println();
				System.out.println();
				System.out.println("Staff : " + staff.getName() + " earns " + String.format("$%.2f", staff.earns()) + " this month");
				System.out.println();
			}
			
			System.out.println("The rosters for courses offered by " + dept.getDepartmentName());
			for(Course crs : dept.getCourseList()){
				System.out.println("\nThe roster for course " + dept.getDepartmentName() + crs.getCourseNumber());
				crs.printRoster();
			}
		}
	}
	
	public String printAllToString(){
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream stream;
		try {
			stream = new PrintStream(output, true, "utf-8");
			stream.println("\nList of departments:");
			printDeptList(stream);
			stream.println();
			
			stream.println("Classroom list:");
			printClassroomList(stream);
			
			for(Department dept : departmentList){
				stream.println("\nThe professor list for department " 
									+ dept.getDepartmentName());
				dept.printProfessorList(stream);
			}
			
			for(Department dept : departmentList){
				stream.println("\nThe course list for department " 
									+ dept.getDepartmentName());
				dept.printCourseList(stream);
			}
			
			for(Classroom room : classroomList){
				stream.println("\nThe schedule for classroom " + room.getRoomNumber());
				room.printSchedule(stream);
			}
			
			for(Department dept : departmentList){
				
				stream.println("\nDepartment " + dept.getDepartmentName());
				stream.println();
				
				stream.println("Printing Professor schedules:");
				stream.println();
				for(Professor prof : dept.getProfessorList()){
					stream.println("The schedule for Prof. " + prof.getName() + ":");
					prof.printSchedule(stream);
					stream.println();
				}
				
				stream.println("Printing Student Schedules:");
				stream.println();
				for(Student stu : dept.getStudentList()){
					stream.println("The schedule for Student " + stu.getName() + ":");
					stu.printSchedule(stream);
					stream.println();
				}
				
				stream.println("Printing Staff Schedules:");
				stream.println();
				for(Staff staff : dept.getStaffList()){
					stream.println("The schedule for Employee " + staff.getName() + ":");
					staff.printSchedule(stream);
					stream.println();
				}
				
				stream.println("The rosters for courses offered by " + dept.getDepartmentName());
				for(Course crs : dept.getCourseList()){
					stream.println("\nThe roster for course " + dept.getDepartmentName() + crs.getCourseNumber());
					crs.printRoster(stream);
				}
			}
			return new String(output.toByteArray(), StandardCharsets.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void printDeptList(){
		for(Department dept : departmentList){
			System.out.println(dept.getDepartmentName());
		}
	}
	
	public void printClassroomList(){
		for(Classroom room : classroomList){
			System.out.println(room.getRoomNumber());
		}
	}
	
	public void printDeptList(PrintStream stream){
		for(Department dept : departmentList){
			stream.println(dept.getDepartmentName());
		}
	}
	
	public void printClassroomList(PrintStream stream){
		for(Classroom room : classroomList){
			stream.println(room.getRoomNumber());
		}
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
