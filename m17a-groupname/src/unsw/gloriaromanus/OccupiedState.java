package unsw.gloriaromanus;

import java.io.Serializable;

public class OccupiedState implements State, Serializable {
    ManufactureBuilding mBuilding;

    public OccupiedState(ManufactureBuilding mBuilding) {
      	this.mBuilding = mBuilding;
    }

    public void start(Production production) {
      	//give user a message "The current state is occupied, user has to wait until current production is finished or cancel it"
    }

    public void cancel() {
		mBuilding.setTask(null);
		mBuilding.setState(mBuilding.getVacant());
	}

	public void repair() {
		//give user a message "The current state is intact, there is nothing to repair"
	}

	public void destroyed() {
		mBuilding.setTask(null);
		mBuilding.setState(mBuilding.getDestroyed());
	}

	public void complete() {
		mBuilding.setTask(null);
		mBuilding.setState(mBuilding.getVacant());
	}

	@Override
	public String toString() {
		return "state : occupied";
	}
}
