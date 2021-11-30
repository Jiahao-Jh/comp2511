package unsw.gloriaromanus;

import java.io.Serializable;

public class RepairState implements State, Serializable {
    ManufactureBuilding mBuilding;

    public RepairState(ManufactureBuilding mBuilding) {
        this.mBuilding = mBuilding;
    }

    public void start(Production production) {
        //give user a message "repairing this building"
    }

    public void cancel() {
        mBuilding.setState(mBuilding.getDestroyed());
    }

    public void repair() {
        //give user a message "already repairing"
    }

    public void destroyed() {
        mBuilding.setState(mBuilding.getDestroyed());
    }

    public void complete() {
        mBuilding.setState(mBuilding.getVacant());
    }

    @Override
    public String toString() {
        return "state : repairing";
    }
}
