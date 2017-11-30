package org.university.software;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.swing.*;

import org.university.hardware.Department;
import org.university.people.Student;

public class UniversityGUI{
	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu adminMenu;
	private JMenu studentMenu;
	private JMenu fileMenu;
	private University univ;
	
	private JMenuItem fileSave;
	private JMenuItem fileLoad;
	private JMenuItem fileExit;
	
	private JMenuItem adminPrintAll;
	
	private JMenuItem studentAdd;
	private JMenuItem studentDrop;
	private JMenuItem studentPrint;
	
	public UniversityGUI(String title, University univ){
		frame = new JFrame(title);
		this.univ = univ;
		frame.setSize(300, 100);
		
		frame.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		frame.add(new JLabel("<HTML><center>Welcome to the University." +
				"<BR>Choose an action from the above menus.</center></HTML>"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildGUI();	
		frame.setVisible(true);
	}
	
	public void buildGUI(){
		menuBar = new JMenuBar();
     	
		// Employee Student Menu
		
		adminMenu = new JMenu("Administrators");
		studentMenu = new JMenu("Students");
		fileMenu = new JMenu("File");
		
		adminPrintAll = new JMenuItem("Print All Info");
		adminPrintAll.addActionListener(new MenuListener());		
	    
		adminMenu.add(adminPrintAll);
		
		studentAdd = new JMenuItem("Add Course");
		studentAdd.addActionListener(new MenuListener());
		studentDrop = new JMenuItem("Drop Course");
		studentDrop.addActionListener(new MenuListener());
		studentPrint = new JMenuItem("Print Schedule");
		studentPrint.addActionListener(new MenuListener());
		
		studentMenu.add(studentAdd);
		studentMenu.add(studentDrop);
		studentMenu.add(studentPrint);
		
		fileSave = new JMenuItem("Save");
		fileLoad = new JMenuItem("Load");
		fileExit = new JMenuItem("Exit");
		
		fileSave.addActionListener(new MenuListener());
		fileLoad.addActionListener(new MenuListener());
		fileExit.addActionListener(new MenuListener());
		
		fileMenu.add(fileSave);
		fileMenu.add(fileLoad);
		fileMenu.add(fileExit);
			
		menuBar.add(fileMenu);
		menuBar.add(studentMenu);
	    menuBar.add(adminMenu);
	
	    frame.setJMenuBar(menuBar);
	}
	
	private class MenuListener implements ActionListener{
		
		
		//@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem)(e.getSource());
			if(source.equals(adminPrintAll)){
				handlePrintAll();
			} else if(source.equals(fileSave)){
				University.saveData(univ);
			} else if(source.equals(fileLoad)){
				univ = University.loadData();
			} else if(source.equals(fileExit)){
				frame.dispose();
			} else if(source.equals(studentAdd)){
				handleAdd();
			} else if(source.equals(studentDrop)){
				handleDrop();
			} else if(source.equals(studentPrint)){
				handlePrint();
			} 
		}
		
		public void handlePrintAll(){
			JTextArea allText = new JTextArea(univ.printAllToString());
			JScrollPane scrollPane = new JScrollPane(allText);
			allText.setLineWrap(true);
			allText.setWrapStyleWord(true);
			scrollPane.setPreferredSize(new Dimension(500,500));
			JOptionPane.showMessageDialog(null, scrollPane, 
					"University Info", JOptionPane.PLAIN_MESSAGE);
		}
		
		public void handleAdd(){
			ArrayList<String> info = getStudentCourseSelect("Add");
			if(info.size() == 0)
				return;
			String sName = info.get(0);
			String deptName = info.get(1);
			int courseNum = Integer.parseInt(info.get(2));
			Student addTo = univ.getStudent(sName);
			if(addTo == null){
				JOptionPane.showMessageDialog(null, "Student \"" + sName + 
						"\" doesn't exist.", "Error adding student to course", 
						JOptionPane.PLAIN_MESSAGE);
				return;
			}
			Department dept = univ.getDept(deptName);
			if(dept == null){
				JOptionPane.showMessageDialog(null, "Department \"" + deptName + 
						"\" doesn't exist.", "Error adding student to course", 
						JOptionPane.PLAIN_MESSAGE);
				return;
			}
			Course crs = dept.getCourse(courseNum);
			if(crs == null){
				JOptionPane.showMessageDialog(null, "Course \"" + deptName + courseNum + 
						"\" doesn't exist.", "Error adding student to course", 
						JOptionPane.PLAIN_MESSAGE);
				return;
			}
			if(!addTo.addCourseBool(crs)){
				Course conflict = addTo.getConflict(crs);
				String conflictString = conflict.getDepartment().getDepartmentName() 
						+ conflict.getCourseNumber();
				JOptionPane.showMessageDialog(null, deptName + courseNum + 
						" course cannot be added to " + sName + "'s Schedule. " 
						+ deptName + courseNum + " conflicts with " + conflictString + ".", 
						"Error adding student to course", JOptionPane.PLAIN_MESSAGE);
				return;
			}
		}
		
		public void handleDrop(){
			ArrayList<String> info = getStudentCourseSelect("Drop");
			if(info.size() == 0)
				return;
			String sName = info.get(0);
			String deptName = info.get(1);
			int courseNum = Integer.parseInt(info.get(2));
			Student dropFrom = univ.getStudent(sName);
			if(dropFrom == null){
				JOptionPane.showMessageDialog(null, "Student \"" + sName + 
						"\" doesn't exist.", "Error dropping student from course", 
						JOptionPane.PLAIN_MESSAGE);
				return;
			}
			Course crs = dropFrom.getCourse(deptName, courseNum);
			if(crs == null){
				JOptionPane.showMessageDialog(null, sName + 
						" is not enrolled in " + deptName + courseNum + ".", 
						"Error dropping student from course", JOptionPane.PLAIN_MESSAGE);
				return;
			}
			dropFrom.dropCourseBool(crs);
		}
		
		public void handlePrint(){
			String studentName = JOptionPane.showInputDialog(null, 
					"Student Name:", "Print Student's Schedule.", JOptionPane.QUESTION_MESSAGE);
			Student stu = univ.getStudent(studentName);
			if(stu == null){
				JOptionPane.showMessageDialog(null, "Student \"" + studentName + "\" doesn't exist.", 
						"Error printing student schedule.", JOptionPane.PLAIN_MESSAGE);
				return;
			}
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			PrintStream stream;
			try {
				stream = new PrintStream(output, true, "utf-8");
				stu.printSchedule(stream);
				String schedule = new String(output.toByteArray(), StandardCharsets.UTF_8);
				JOptionPane.showMessageDialog(null, schedule, 
						"Student " + studentName + "'s Schedule.", JOptionPane.PLAIN_MESSAGE);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		public ArrayList<String> getStudentCourseSelect(String addOrDrop){
			ArrayList<String> toReturn = new ArrayList<String>();
			JPanel studentCourseSelect = new JPanel();
			studentCourseSelect.setLayout(new GridLayout(3, 2));
			JTextField name = new JTextField();
			name.setPreferredSize(new Dimension(100, 25));
			JTextField dept = new JTextField();
			dept.setPreferredSize(new Dimension(100, 25));
			JTextField num = new JTextField();
			num.setPreferredSize(new Dimension(100, 25));
			studentCourseSelect.add(new JLabel("Student Name:"));
			studentCourseSelect.add(name);
			studentCourseSelect.add(new JLabel("Department:"));
			studentCourseSelect.add(dept);
			studentCourseSelect.add(new JLabel("Course #:"));
			studentCourseSelect.add(num);
			int result = JOptionPane.showConfirmDialog(null, studentCourseSelect, addOrDrop + " Course", JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION){
				String sName = name.getText();
				String deptName = dept.getText();
				String crsNum = num.getText();
				toReturn.add(sName);
				toReturn.add(deptName);
				toReturn.add(crsNum);
			}
			return toReturn;
		}
		
	}
}
