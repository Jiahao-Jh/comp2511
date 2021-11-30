package unsw.gloriaromanus;

public interface Battle_subject {
	public void attach(Battle_observer o);
	public void detach(Battle_observer o);
	public void notifyObservers(String message);
}
