package staff;

import java.sql.Date;

/**
 * A staff member
 * 
 * @author jiahao zhang
 *
 */
public class StaffMember {
    String name;
    int salary;
    Date hire_date;
    Date end_date;

    /**
     * No-arg constructor
     */
    public StaffMember() {

    }

    /**
     * Two-arg constructor
     * 
     * @param name
     * @param salar
     */
    public StaffMember(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    /**
     * Four-arg constructor
     * 
     * @param name
     * @param salar
     * @param hire_date
     * @param end_date
     */
    public StaffMember(String name, int salary, Date hire_date, Date end_date) {
        this.name = name;
        this.salary = salary;
        this.hire_date = hire_date;
        this.end_date = end_date;
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
        StaffMember tmp = (StaffMember) obj;
        if ( this.name.equals(tmp.name)  && this.salary == tmp.salary && this.hire_date.equals(tmp.hire_date) 
        && this.end_date.equals(tmp.end_date)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String msg = "Name: " + this.getName() + " Salary: " + this.getSalary();
        if (this.getHire_date() != null){
            msg += " Hire_date: " + this.getHire_date();
        }
        if (this.getEnd_date() != null){
            msg += " End_date: " + this.getEnd_date();
        }
        return msg;
    }

    /**
     * 
     * @return staff member's name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return staff member's salary
     */
    public int getSalary() {
        return salary;
    }

    /**
     * 
     * @param salary
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
     * 
     * @return staff member's hire_date
     */
    public Date getHire_date() {
        return hire_date;
    }

    /**
     * 
     * @param hire_date
     */
    public void setHire_date(Date hire_date) {
        this.hire_date = hire_date;
    }

    /**
     * 
     * @return staff member's end_date
     */
    public Date getEnd_date() {
        return end_date;
    }

    /**
     * 
     * @param end_date
     */
    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

}
