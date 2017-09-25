package university;
import java.util.ArrayList;

public class Student {
	private String name;
	private ArrayList<Course> courses;
	private Department dept;
	public Student(){
		courses = new ArrayList<Course>();
		dept = new Department();
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public void setDepartment(Department dept){
		this.dept = dept;
		if(!this.dept.getStudents().contains(this)){
			this.dept.addStudent(this);
		}
	}
	public Department getDepartment(){
		return dept;
	}
	public void addCourse(Course crs){
		courses.add(crs);
		if(!crs.getStudentRoster().contains(this))
			crs.addStudent(this);
	}
	public ArrayList<Course> getCourses(){
		return courses;
	}
}
