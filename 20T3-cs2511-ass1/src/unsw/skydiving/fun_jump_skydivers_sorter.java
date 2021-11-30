package unsw.skydiving;
import java.util.Comparator;

public class fun_jump_skydivers_sorter implements Comparator<Student>{
    @Override
    public int compare(Student input1, Student input2) {
        return input1.getName().compareTo(input2.getName());
    }
}
