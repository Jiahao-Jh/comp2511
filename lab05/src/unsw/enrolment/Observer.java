package unsw.enrolment;

public interface Observer {
	
	public void update(String coursecode , String term, String zid, String mark_name, double mark);
	
}
