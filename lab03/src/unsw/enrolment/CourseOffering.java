package unsw.enrolment;
import java.util.ArrayList;
import java.util.List;

public class CourseOffering {

    private Course course;
    private String term;
    private List<Session> sessions;
    private List<Enrolment> enrolments;

    public CourseOffering(Course course, String term) {
        this.course = course;
        this.term = term;
        this.sessions = new ArrayList<>();
        this.enrolments = new ArrayList<>();
        this.course.addOffering(this);
    }

    public List<Course> getPrereqs() {
        return course.getPrereqs();
    }

    public void addEnrolment(Enrolment Enrolment) {
        enrolments.add(Enrolment);
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public List<Session> getSession() {
        return sessions;
    }

    public Course getCourse() {
        return course;
    }

    public String getTerm() {
        return term;
    }

}
