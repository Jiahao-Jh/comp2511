package unsw.enrolment;

public class Enrolment {

    private CourseOffering offering;
    private Grade grade;
    private Student student;

    public Enrolment(CourseOffering offering, Student student) throws Exception{
        for (Course tmp : offering.getPrereqs()){
            int flag = 0;

            for (Enrolment temp : student.getEnrolments()){
                if (temp.getGrade() == null){
                    continue;
                }
                if (tmp.equals(temp.getCourse()) && temp.getGrade().CheckPass() ){
                    flag=1;
                    continue;
                }
            }
            if (flag == 0){
                throw new Exception("student have not passed all prerequisite");
            }
        }
        this.offering = offering;
        this.student = student;
        this.offering.addEnrolment(this);
        this.student.addEnrolment(this);
    }



    public Course getCourse() {
        return offering.getCourse();
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String getTerm() {
        return offering.getTerm();
    }

}

