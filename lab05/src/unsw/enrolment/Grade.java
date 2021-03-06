package unsw.enrolment;

public class Grade {
    private double mark;
    private String grade;

    public Grade(double mark) {
        this.mark = mark;
        if (mark < 50)
            grade = "FL";
        else if (mark < 65)
            grade = "PS";
        else if (mark < 75)
            grade = "DN";
        else
            grade = "HD";
    }

    public boolean isPassing() {
        return mark >= 50;
    }
}
