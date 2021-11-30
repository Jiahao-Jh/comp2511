package unsw.skydiving;

public class Training_jump {
    private String id;
    private Student trainee;
    private Instructor instructor;
    private Flight flight;

	public Training_jump(String id, Flight flight, Student trainee, Instructor instructor) {
        this.id = id;
        this.flight = flight;
        this.trainee = trainee;
        this.instructor = instructor;
	}
    
    public String getId() {
        return id;
    }

    public String getTrainee_name() {
        return trainee.getName();
    }

    public String getInstructor_name() {
        return instructor.getName();
    }

    public void remove_busytime() {
        trainee.removeBusytime(flight.getStarttime(), flight.getEndtime().plusMinutes(15));
        instructor.removeBusytime(flight.getStarttime(), flight.getEndtime().plusMinutes(25));
    }
}