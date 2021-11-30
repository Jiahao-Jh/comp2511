package unsw.enrolment;
import java.util.ArrayList;
import java.util.List;

/**
 * A course in the enrolment system.
 * @author Robert Clifton-Everest
 *
 */
public class Course {

    private String courseCode;
    private String title;
    private int uoc;
    private List<Course> prereqs;
    private List<CourseOffering> courseOfferings;


    public Course(String courseCode, String title) {
        this.courseCode = courseCode;
        this.title = title;
        this.prereqs = new ArrayList<Course>();
        this.courseOfferings = new ArrayList<CourseOffering>();
    }

    public void addPrereq(Course course) {
        prereqs.add(course);
    }

    public void addOffering(CourseOffering offering) {
        courseOfferings.add(offering);
    }

    public String getCourseCode() {
        return courseCode;
    }

    public int getUOC() {
        return uoc;
    }

	public String getTitle() {
		return title;
    }

    public List<Course> getPrereqs() {
        return prereqs;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (obj == this){
            return true;
        }

        if (this.getClass() != obj.getClass()){
            return false;
        }
        Course tmp = (Course) obj;
        if ( this.courseCode.equals(tmp.courseCode)  && this.uoc == tmp.uoc && this.title.equals(tmp.title)){
            return true;
        } else {
            return false;
        }
    }
}
