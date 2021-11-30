package unsw.enrolment;

import java.util.ArrayList;
import java.util.List;

public class Enrolment implements MarkSubject {

    private CourseOffering offering;
    private Grade grade;
    private Student student;
    private List<Session> sessions;
    private MarkComponent mark;
    private List<Observer> observers;


    public Enrolment(CourseOffering offering, Student student, Session... sessions) {
        this.offering = offering;
        this.student = student;
        this.grade = null; // Student has not completed course yet.
        this.mark = null;
        student.addEnrolment(this);
        offering.addEnrolment(this);
        this.sessions = new ArrayList<>();
        this.observers = new ArrayList<>();
        for (Session session : sessions) {
            this.sessions.add(session);
        }
    }

    public Course getCourse() {
        return offering.getCourse();
    }

    public String getCoursecode() {
        return offering.getCourse().getCourseCode();
    }


    public String getTerm() {
        return offering.getTerm();
    }

    public boolean hasPassed() {
        return grade != null && grade.isPassing();
    }

    public String getStudentID() {
        return student.getZID();
    }

    public void assignMark(MarkComponent mark) {
        this.mark = mark;
        grade = new Grade(mark.SumMark());
        notifyObservers(mark);
    }

    public String getMarkName() {
        return mark.getName();
    }
    public double getMark() {
        return mark.SumMark();
    }

    @Override
	public void attach(Observer o){
        observers.add(o);
    }

    @Override
	public void detach(Observer o){
        observers.remove(o);
    }

    @Override
	public void notifyObservers(MarkComponent mark){
        if (mark.getClass() == MarkComposite.class){
            for(Observer obs : observers) {
                obs.update(this.getCoursecode(),this.getTerm(),this.getStudentID(),((MarkComposite) mark).getName(),((MarkComposite) mark).getMark());
            }
        } else if (mark.getClass() == MarkLeaf.class){
            for(Observer obs : observers) {
                obs.update(this.getCoursecode(),this.getTerm(),this.getStudentID(),((MarkLeaf) mark).getName(),((MarkLeaf) mark).getmark());
            }
        }

    }

}
