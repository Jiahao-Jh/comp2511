package unsw.enrolment;
public interface MarkSubject {
    public void attach(Observer o);
	public void detach(Observer o);
	public void notifyObservers(MarkComponent mark);
}
