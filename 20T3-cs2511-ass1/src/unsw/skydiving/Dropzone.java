package unsw.skydiving;

import java.util.ArrayList;
import java.util.List;

public class Dropzone {
    String dropzone;
    private List<Flight> flights;

    public Dropzone(String dropzone){
        this.dropzone = dropzone;
        flights = new ArrayList<Flight>();
    }

	public String getDropzone() {
		return dropzone;
	}

    public void addFlight(Flight flight) {
        flights.add(flight);
    }


    public int getDropzone_Vacancies() {
        int vacancies = 0;
        for(int i = 0; i < flights.size(); i++) {
            vacancies += flights.get(i).getVacanciesSize();
        }
        return vacancies;
    }
}
