package unsw.skydiving;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Student {

    private String name;
    private List<LocalDateTime> start_time;
    private List<LocalDateTime> end_time;

    public Student(String name) {
      this.name = name;
      start_time = new ArrayList<LocalDateTime>();
      end_time = new ArrayList<LocalDateTime>();
    }
    
    public String getName(){
      return name;
    }

    public boolean equal_name(String name_to_compare){
      return name.equals(name_to_compare);
    }

    /**
     * add busytime to a student 
     * @param starttime
     * @param endtime
     */
    public void addBusytime(LocalDateTime time_input_1, LocalDateTime time_input_2){
      start_time.add(time_input_1);
      end_time.add(time_input_2);
    }

    public void removeBusytime(LocalDateTime time_input_1, LocalDateTime time_input_2){
      start_time.remove(time_input_1);
      end_time.remove(time_input_2);
    }

    public List<LocalDateTime> getStart_time() {
      return start_time;
    }

    /**
     * @return how many dives skydiver participate in 
     */
    public int getStart_time_size_on_day(LocalDate date) {
      int i = 0;
      for (LocalDateTime time : start_time) {
        if (time.toLocalDate().equals(date)){
          i++;
        }
      }
      return i;
    }

    /**
     * check if student free at given time intervel
     * @param starttime
     * @param endtime
     */
    public boolean check_student(LocalDateTime starttime, LocalDateTime endtime) {
      for(int i = 0; i < start_time.size(); i++) {
        if ((start_time.get(i).isBefore(starttime) && end_time.get(i).isAfter(starttime))
            || (start_time.get(i).isBefore(endtime) && end_time.get(i).isAfter(endtime)) 
            || start_time.get(i).equals(starttime) || end_time.get(i).equals(endtime)){
            return false;
        }
      }
      return true;
    }
}