package unsw.gloriaromanus;

public interface State {
	void start(Production production);
	void cancel();
	void repair();
	void destroyed();
	void complete();
}
