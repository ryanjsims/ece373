//package org.university.software;
//
//import org.university.hardware.Classroom;
//import org.university.hardware.Department;
//import org.university.people.Professor;
//import org.university.people.Student;
//import org.university.people.Staff;
//
///* Test driver by Lahiru Ariyananda and Peter Hall */
//
//public class Driver1 
//{
//
//	public static void main(String[] args) 
//	{
//
//		/* Initialize University */
//
//		University univ = new University();
//
//		/*
//		 * Create University of department, classrooms, professors,
//		 * students, staff.
//		 */
//
//		/* Set Department */
//
//		Department dept1 = new Department();
//		Department dept2 = new Department();
//
//		/* Set Student */
//
//		Student s1 = new Student();
//		Student s2 = new Student();
//		Student s3 = new Student();
//		Student s4 = new Student();
//
//		/* Set Course */
//
//		Course c1 = new Course();
//		Course c2 = new Course();
//		Course c3 = new Course();
//		Course c4 = new Course();
//		Course c5 = new Course();
//		Course c6 = new Course();
//		Course c7 = new Course();
//		Course c8 = new Course();
//
//		/* Set Room */
//
//		Classroom cr1 = new Classroom();
//		Classroom cr2 = new Classroom();
//		Classroom cr3 = new Classroom();
//		Classroom cr4 = new Classroom();
//
//		/* Set Professor */
//
//		Professor p1 = new Professor();
//		Professor p2 = new Professor();
//		Professor p3 = new Professor();
//		Professor p4 = new Professor();
//		Professor p5 = new Professor();
//
//		/* Set Staff */
//
//		Staff sf1 = new Staff();
//
//		/* Set Attributes */
//
//		dept1.setDepartmentName("ECE");
//		dept2.setDepartmentName("CS");
//
//		s1.setName("Lahiru");
//		dept1.addStudent(s1);
//		s2.setName("Daz");
//		dept1.addStudent(s2);
//		s3.setName("Ben");
//		dept2.addStudent(s3);
//		s4.setName("Jerry");
//		dept2.addStudent(s4);
//
//		c1.setCourseNumber(387);
//		c1.setName("Enterprise Web Applications");
//		dept2.addCourse(c1);
//
//		c2.setCourseNumber(372);
//		c2.setName("Comparative Programming Languages");
//		dept2.addCourse(c2);
//
//		c3.setCourseNumber(345);
//		c3.setName("Discrete Structures");
//		dept2.addCourse(c3);
//
//		c4.setCourseNumber(426);
//		c4.setName("Computer Networks");
//		dept2.addCourse(c4);
//
//		c5.setCourseNumber(275);
//		c5.setName("Computer Programming II");
//		dept1.addCourse(c5);
//
//		c6.setCourseNumber(320);
//		c6.setName("Circuits Analysis");
//		dept1.addCourse(c6);
//
//		c7.setCourseNumber(373);
//		c7.setName("Object Oriented Software Design");
//		dept1.addCourse(c7);
//
//		c8.setCourseNumber(107);
//		c8.setName("Experimental Course");
//		dept1.addCourse(c8);
//
//		cr1.setRoomNumber("Harvill 101");
//		cr2.setRoomNumber("Harvill 203");
//		cr3.setRoomNumber("Gould Simpson 102");
//		cr4.setRoomNumber("Gould Simpson 105");
//
//		p1.setName("Regan");
//		dept1.addProfessor(p1);
//		p2.setName("RosenBlit");
//		dept1.addProfessor(p2);
//		p3.setName("Tharp");
//		dept1.addProfessor(p3);
//		p4.setName("Kececioglu");
//		dept2.addProfessor(p4);
//		p5.setName("Homer");
//		dept2.addProfessor(p5);
//
//		sf1.setName("Carol");
//		dept2.addStaff(sf1);
//
//		univ.departmentList.add(dept1);
//		univ.departmentList.add(dept2);
//
//		univ.classroomList.add(cr1);
//		univ.classroomList.add(cr2);
//		univ.classroomList.add(cr3);
//		univ.classroomList.add(cr4);
//
//		/* Initialization ending */
//
//		/* Test1 beginning */
//
//		c1.setSchedule(201);
//		c1.setSchedule(401);
//		c2.setSchedule(202);
//		c2.setSchedule(402);
//		c3.setSchedule(303);
//		c3.setSchedule(503);
//		c4.setSchedule(102);
//		c4.setSchedule(302);
//		c5.setSchedule(303);
//		c5.setSchedule(503);
//		c6.setSchedule(102);
//		c6.setSchedule(302);
//		c7.setSchedule(201);
//		c7.setSchedule(401);
//		c8.setSchedule(102);
//
//
//		/* Sign up professors */
//
//		/* Sign up Prof. Tharp for class ECE320 */
//
//		p3.addCourse(c6);
//
//		/* Sign up Prof. Kececioglu for class CS387 */
//
//		p4.addCourse(c1);
//
//		/* Sign up Prof. Kececioglu for class CS372 */
//
//		p4.addCourse(c2);
//
//		/* Sign up Prof. Homer for class CS345 */
//
//		p5.addCourse(c3);
//
//		/* Sign up Prof. Homer for class CS426 */
//
//		p5.addCourse(c4);
//
//		/* Set classrooms for courses */
//
//		c1.setRoomAssigned(cr2);
//		c2.setRoomAssigned(cr4);
//		c3.setRoomAssigned(cr3);
//		c4.setRoomAssigned(cr3);
//		c6.setRoomAssigned(cr1);
//
//		/* Add course to students */
//
//		s1.addCourse(c4);
//		s1.addCourse(c1);
//
//		s2.addCourse(c2);
//		s2.addCourse(c3);
//		s2.addCourse(c8);
//
//		s3.addCourse(c1);
//		s3.addCourse(c2);
//		s3.addCourse(c3);
//		s3.addCourse(c4);
//
//		s4.addCourse(c1);
//		s4.addCourse(c2);
//		s4.addCourse(c6);
//
//		System.out.println("Testing Professor Conflicts  ...");
//		p3.addCourse(c1);
//		p4.addCourse(c3);
//
//		System.out.println("\nTesting dropCourse method in Student class ...");
//		s1.dropCourse(c7);
//		s1.addCourse(c5);
//		s1.dropCourse(c5);
//		s2.dropCourse(c8);
//
//		/* Test conflicts of student schedule */
//		System.out.println("\nConflicts:");
//
//		/* Test conflicts of classroom schedule */
//		c8.setRoomAssigned(cr1);
//
//		
//		/* Test conflicts of professor schedule */
//
//		p3.addCourse(c8);
//		p5.addCourse(c5);
//
//		/* Try to add ECE320 to student Lahiru */
//
//		s1.addCourse(c6);
//
//		/* Try to add ECE373 to student Lahiru */
//
//		s1.addCourse(c7);
//
//		s2.addCourse(c5);
//
//		/* Add course to staff */
//
//		sf1.addCourse(c1);
//
//		/* Test adding more than 1 course */
//
//		sf1.addCourse(c2);
//
//		
//		/* Schedules */
//
//		System.out.println("\nThe schedule for Prof. " + univ.departmentList.get(0).getProfessorList().get(2).getName() + ":");
//		univ.departmentList.get(0).getProfessorList().get(2).printSchedule();
//
//		System.out.println("\nThe schedule for Prof. " + univ.departmentList.get(1).getProfessorList().get(0).getName() + ":");
//		univ.departmentList.get(1).getProfessorList().get(0).printSchedule();
//
//		System.out.println("\nThe schedule for Prof. " + univ.departmentList.get(1).getProfessorList().get(1).getName() + ":");
//		univ.departmentList.get(1).getProfessorList().get(1).printSchedule();
//
//		/* Print schedule of classroom 1 */
//
//		System.out.println("\nThe schedule for " + univ.classroomList.get(0).getRoomNumber() + ":");
//		univ.classroomList.get(0).printSchedule();
//
//		/* Print schedule of classroom 2 */
//
//		System.out.println("\nThe schedule for " + univ.classroomList.get(1).getRoomNumber() + ":");
//		univ.classroomList.get(1).printSchedule();
//
//		/* Print schedule of classroom 3 */
//
//		System.out.println("\nThe schedule for " + univ.classroomList.get(2).getRoomNumber() + ":");
//		univ.classroomList.get(2).printSchedule();
//
//		/* Print schedule of classroom 4 */
//
//		System.out.println("\nThe schedule for " + univ.classroomList.get(3).getRoomNumber() + ":");
//		univ.classroomList.get(3).printSchedule();
//
//		/* Student schedules */
//
//		System.out.println("\nThe schedule for Student " + univ.departmentList.get(0).getStudentList().get(0).getName() + ":");
//		univ.departmentList.get(0).getStudentList().get(0).printSchedule();
//
//		System.out.println("\nThe schedule for Student " + univ.departmentList.get(1).getStudentList().get(0).getName() + ":");
//		univ.departmentList.get(1).getStudentList().get(0).printSchedule();
//
//		System.out.println("\nThe schedule for Student " + univ.departmentList.get(1).getStudentList().get(1).getName() + ":");
//		univ.departmentList.get(1).getStudentList().get(1).printSchedule();
//
//		/* Staff schedules */
//
//		System.out.println("\nThe schedule for Employee " + univ.departmentList.get(1).getStaffList().get(0).getName() + ":");
//		univ.departmentList.get(1).getStaffList().get(0).printSchedule();
//		
//		/* Print schedule of classroom 4 */
//
//		System.out.println("\nThe schedule for course " + univ.departmentList.get(1).getCourseList().get(0).getName() + ":");
//		univ.departmentList.get(1).getCourseList().get(0).printSchedule();
//
//	}
//}