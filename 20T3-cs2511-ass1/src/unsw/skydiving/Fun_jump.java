package unsw.skydiving;
import java.util.List;
import org.json.JSONArray;

public class Fun_jump {
    private String id;
    private Flight flight;
    private List<Student> licenced_jumpers;
    

	public Fun_jump(String id, Flight flight, List<Student> licenced_jumpers) {
        this.id = id;
        this.flight = flight;
        this.licenced_jumpers = licenced_jumpers;
	}

    public int numof_skydivers() {
        return licenced_jumpers.size();
    }

    public String getId() {
        return id;
    }

    public JSONArray getSkydivers() {
        JSONArray skydivers = new JSONArray();
        licenced_jumpers.sort(new fun_jump_skydivers_sorter());
        for (Student licenced_jumper : licenced_jumpers) {
            skydivers.put(licenced_jumper.getName());
        }
        return skydivers;
    }

    public void remove_busytime() {
        for(int i = 0; i < licenced_jumpers.size(); i++) {
            licenced_jumpers.get(i).removeBusytime(flight.getStarttime(), flight.getEndtime().plusMinutes(10));
        }
    }
    
}
