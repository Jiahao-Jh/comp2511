package unsw.enrolment;

public class Grade {
    private int mark;
    private String grade;

	public Grade(int mark, String grade) {
		this.mark = mark;
		this.grade = grade;
	}

    public int getMark() {
		return mark;
	}

	public String getGrade() {
		return grade;
	}

	public boolean CheckPass() {
		if (grade == "PS" || grade == "HD" || grade == "CR" || grade == "DN"){
			return true;
		} else {
			return false;
		}
	}
}
