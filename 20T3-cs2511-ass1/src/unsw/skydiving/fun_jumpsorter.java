package unsw.skydiving;
import java.util.Comparator;

public class fun_jumpsorter implements Comparator<Fun_jump>{
    @Override
    public int compare(Fun_jump f1, Fun_jump f2) {
        if(f1.numof_skydivers() > (f2.numof_skydivers())) {
            return 1;
         } else if(f1.numof_skydivers() < (f2.numof_skydivers())) {
            return -1;
         } else {
            return 0;
         }
    }
}