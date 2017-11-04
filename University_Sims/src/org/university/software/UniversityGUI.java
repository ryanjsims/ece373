package org.university.software;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class UniversityGUI{
	private JFrame frame;
	private JMenuBar menuBar;		//the horizontal container
	private JMenu adminMenu;		//JMenu objects are added to JMenuBar objects as the "tabs"
	private JMenu studentMenu;
	private JMenu fileMenu;
	private University univ;
	
	private JMenuItem fileSave; 		//JMenuItem objects are added to JMenu objects as the drop down selections.
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
		adminPrintAll.addActionListener(new MenuListener());		//The innerclass is implementing ActionListener, so it can take action when the JMenuItem is clicked.
	    
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

		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem)(e.getSource());
			if(source.equals(adminPrintAll)){
				handlePrintAll();
			} else if(source.equals(fileExit)){
				frame.dispose();
			}
		}
		
		public void handlePrintAll(){
			JTextArea allText = new JTextArea(univ.printAllToString());
			JScrollPane scrollPane = new JScrollPane(allText);
			allText.setLineWrap(true);
			allText.setWrapStyleWord(true);
			scrollPane.setPreferredSize(new Dimension(500,500));
			JOptionPane.showMessageDialog(null, scrollPane, "University Info", JOptionPane.PLAIN_MESSAGE);
		}
		
	}
}
