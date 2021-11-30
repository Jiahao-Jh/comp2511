package unsw.enrolment.test;

import unsw.enrolment.Course;
import unsw.enrolment.CourseOffering;
import unsw.enrolment.Lecture;
import unsw.enrolment.Session;
import unsw.enrolment.Student;
import unsw.enrolment.Enrolment;
import unsw.enrolment.Grade;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class EnrolmentTest {

    public static void main(String[] args) throws Exception {

        // Create courses
        Course comp1511 = new Course("COMP1511", "Programming Fundamentals");
        Course comp1531 = new Course("COMP1531", "Software Engineering Fundamentals");
        comp1531.addPrereq(comp1511);
        Course comp2521 = new Course("COMP2521", "Data Structures and Algorithms");
        comp2521.addPrereq(comp1511);

        CourseOffering comp1511Offering = new CourseOffering(comp1511, "19T1");
        CourseOffering comp1531Offering = new CourseOffering(comp1531, "19T1");
        CourseOffering comp2521Offering = new CourseOffering(comp2521, "19T2");

        //Create some sessions for the courses
        DayOfWeek a = DayOfWeek.FRIDAY;
        LocalTime b = LocalTime.of(0, 0);
        LocalTime c = LocalTime.of(2, 0);
        Lecture comp2521lecture = new Lecture("Main building", a, b, c, "Ashesh");
        Session comp2521sSession = (Session) comp2521lecture;
        comp2521Offering.addSession(comp2521sSession);
        System.out.println(comp2521Offering.getSession());
        //Create a student
        Student jiahao = new Student("z1234567");

        //Enrol the student in COMP1511 for T1 (this should succeed)
        Enrolment jiahao_comp1511Offering = new Enrolment(comp1511Offering, jiahao);
        System.out.println(jiahao_comp1511Offering.getTerm());

        //Enrol the student in COMP1531 for T1 (this should fail as they
        // have not met the prereq)

        //this will throw exceptions 
        Enrolment jiahao_comp1531Offering = new Enrolment(comp1531Offering, jiahao);

        //Give the student a passing grade for COMP1511
        Grade jiahao_comp1511Offering_grade = new Grade(85,"HD");
        jiahao_comp1511Offering.setGrade(jiahao_comp1511Offering_grade);
        System.out.println(jiahao_comp1511Offering_grade.getMark());

        //Enrol the student in 2521 (this should succeed as they have met
        // the prereqs)
        Enrolment jiahao_comp2521Offering = new Enrolment(comp2521Offering, jiahao);
        System.out.println(jiahao_comp2521Offering.getTerm());
    }
}
