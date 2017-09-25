package university;
import java.util.ArrayList;

public class Course {
	private String name;
	private int number;
	private ArrayList<Integer> schedule;
	private ArrayList<Student> roster;
	private Department dept;
	
	public Course(){
		schedule = new ArrayList<Integer>();
		roster = new ArrayList<Student>();
		dept = new Department();
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public void setCourseNumber(int num){
		number = num;
	}
	public int getCourseNumber(){
		return number;
	}
	public void setSchedule(int addNum){
		schedule.add(addNum);
	}
	public ArrayList<Integer> getSchedule(){
		return schedule;
	}
	public void setDepartment(Department dept){
		this.dept = dept;
		if(!this.dept.getCourses().contains(this))
			this.dept.addCourse(this);
	}
	public Department getDepartment(){
		return dept;
	}
	public void addStudent(Student student){
		roster.add(student);
		if(!student.getCourses().contains(this)){
			student.addCourse(this);
		}
	}
	public ArrayList<Student> getStudentRoster(){
		return roster;
	}
}
