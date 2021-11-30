package staff;

import java.sql.Date;

public class Lecturer extends StaffMember {
    String school;
    String academic_status;

    /**
     * No-arg constructor
     */
    public Lecturer() {
        super();
    }

    /**
     * Four-arg constructor
     * 
     * @param name
     * @param salary
     * @param hire_date
     * @param end_date
     */
    public Lecturer(String name, int salary, Date hire_date, Date end_date) {
        super(name, salary, hire_date, end_date);
    }

    /**
     * Six-arg constructor
     * 
     * @param name
     * @param salary
     * @param hire_date
     * @param end_date
     * @param school
     * @param academic_status
     */
    public Lecturer(String name, int salary, Date hire_date, Date end_date,
     String school, String academic_status) {
        super(name, salary, hire_date, end_date);
        this.school = school;
        this.academic_status = academic_status;
    }


    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false){
            return false;
        }
        if (this.getClass() != obj.getClass()){
            return false;
        }
        Lecturer tmp = (Lecturer) obj;
        if (this.school.equals(tmp.school)  && this.academic_status.equals(tmp.academic_status)){
            return true;
        } else{
            return false;
        }

    }

    @Override
    public String toString() {
        String msg = super.toString();
        if (this.getSchool() != null){
            msg += " School: " + this.getSchool();
        }
        if (this.getAcademic_status() != null){
            msg += " Academic_status: " + this.getAcademic_status();
        }
        return msg;
    }


    /**
     * 
     * @return lecturer's school
     */
    public String getSchool() {
        return school;
    }

    /**
     * 
     * @param school
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * 
     * @return lecturer's academic status
     */
    public String getAcademic_status() {
        return academic_status;
    }

    /**
     * 
     * @param academic_status
     */
    public void setAcademic_status(String academic_status) {
        this.academic_status = academic_status;
    }


    
}
