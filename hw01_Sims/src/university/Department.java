package university;
import java.util.ArrayList;

public class Department {
	private String name;
	private ArrayList<Course> courses;
	private ArrayList<Student> students;
	public Department(){
		courses = new ArrayList<Course>();
		students = new ArrayList<Student>();
	}
	public void setDepartmentName(String name){
		this.name = name;
	}
	public String getDepartmentName(){
		return name;
	}
	public void addStudent(Student student){
		students.add(student);
		if(!student.getDepartment().equals(this))
			student.setDepartment(this);
	}
	public ArrayList<Student> getStudents(){
		return students;
	}
	public void addCourse(Course course){
		courses.add(course);
		if(!course.getDepartment().equals(this))
			course.setDepartment(this);
	}
	public ArrayList<Course> getCourses(){
		return courses;
	}
}
