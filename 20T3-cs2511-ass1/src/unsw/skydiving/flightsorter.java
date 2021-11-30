package unsw.skydiving;
import java.util.Comparator;

public class flightsorter implements Comparator<Flight>{
    @Override
    public int compare(Flight f1, Flight f2) {
        return f1.getStarttime().compareTo(f2.getStarttime());
    }
}
