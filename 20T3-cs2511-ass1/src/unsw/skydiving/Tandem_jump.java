package unsw.skydiving;

public class Tandem_jump {
    private String id;
    private Student passenger;
    private Tandem_master tandem_master;
    private Flight flight;

	public Tandem_jump(String id, Flight flight, Student passenger, Tandem_master tandem_master) {
        this.id = id;
        this.flight = flight;
        this.passenger = passenger;
        this.tandem_master = tandem_master;
	}
    
    public String getId() {
        return id;
    }

    public String getPassenger_name() {
        return passenger.getName();
    }

    public String getTandem_master_name() {
        return tandem_master.getName();
    }

    public void remove_busytime() {
        passenger.removeBusytime(flight.getStarttime().minusMinutes(5), flight.getEndtime());
        tandem_master.removeBusytime(flight.getStarttime().minusMinutes(5), flight.getEndtime().plusMinutes(10));
    }
}
