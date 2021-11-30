package staff.test;

import java.sql.Date;

import staff.Lecturer;
import staff.StaffMember;

public class StaffTest {
    public void printStaffDetails(StaffMember tmp) {
        System.out.println(tmp.toString());
    }

    public static void main(String[] args) {
        // printStaffDetails test
        // creat StaffMember
        Date hire_date = Date.valueOf("2020-09-26");
        Date end_date = Date.valueOf("2020-09-25");
        StaffMember staff1 = new StaffMember("jiahao", 60, hire_date, end_date);
        // creat Lecturer
        Lecturer lecturer1 = new Lecturer("jiahao", 60, hire_date, end_date, "UNSW", "A");
        // call printStaffDetails
        StaffTest b = new StaffTest();
        b.printStaffDetails(staff1);
        b.printStaffDetails(lecturer1);
        // staffmember equal test
        StaffMember staff2 = new StaffMember("jiahao", 60, hire_date, end_date);
        StaffMember staff3 = new StaffMember("jiahao", 60);
        StaffMember staff4 = new StaffMember("jiahao", 60, hire_date, end_date);
        System.out.println(staff1.getHire_date());
        System.out.println(staff1.toString());

        System.out.println(staff1.equals(staff2)); //true
        System.out.println(staff1.equals(staff2)); //true
        System.out.println(staff1.equals(staff1)); //true
        System.out.println(staff2.equals(staff4)); //true
        System.out.println(staff1.equals(staff4)); //true
        System.out.println(staff1.equals(staff3)); //false
        System.out.println(staff1.equals(null)); //false
        // lecturer equal test
        Lecturer lecturer2 = new Lecturer("jiahao", 60, hire_date, end_date, "UNSW", "A");
        Lecturer lecturer3 = new Lecturer("jiahao", 60, hire_date, end_date, "UNSW", "A");
        Lecturer lecturer4 = new Lecturer("jiahao", 60, hire_date, end_date);
        Lecturer lecturer5 = new Lecturer("jiahao", 60, hire_date, end_date, "UNSW", "B");
        System.out.println(lecturer1.equals(lecturer2)); //true
        System.out.println(lecturer1.equals(lecturer2)); //true
        System.out.println(lecturer1.equals(lecturer1)); //true
        System.out.println(lecturer2.equals(lecturer3)); //true
        System.out.println(lecturer1.equals(lecturer3)); //true
        System.out.println(lecturer1.equals(lecturer3)); //true
        System.out.println(lecturer2.equals(lecturer4)); //false
        System.out.println(lecturer3.equals(lecturer5)); //false
        System.out.println(lecturer1.equals(null)); //false
    }
}
