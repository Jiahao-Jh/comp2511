package unsw.gloriaromanus;

import java.io.Serializable;

public class VacantState implements State, Serializable {
	ManufactureBuilding mBuilding;

	public VacantState(ManufactureBuilding mBuilding) {
		this.mBuilding = mBuilding;
	}   
	
	public void start(Production production) {
		mBuilding.setState(mBuilding.getOccupied());
		mBuilding.setTask(production);
	}

	public void cancel() {
		//give user a message "The current state is vacant, there is nothing to cancel"
	}

	public void repair() {
		//give user a message "The current state is intact, there is nothing to repair"
	}

	public void destroyed() {
		mBuilding.setState(mBuilding.getDestroyed());
	}

	public void complete() {
		// nothing to complete
	}

	@Override
	public String toString() {
		return "state : vacant";
	} 
}
