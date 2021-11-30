package unsw.gloriaromanus;

import java.io.Serializable;

public class DestroyedState implements State, Serializable {

    private static final long serialVersionUID = 1L;
    ManufactureBuilding mBuilding;

    public DestroyedState(ManufactureBuilding mBuilding) {
        this.mBuilding = mBuilding;
    }

    public void start(Production production) {
        //give user a message "this building was destroyed, need repair"
    }

    public void cancel() {
        //give user a message "this building was destroyed, need repair"
    }

    public void repair() {
        mBuilding.setState(mBuilding.getRepair());
        mBuilding.setRepair();
    }

    public void destroyed() {
        // this building is already destroyed
    }

    public void complete() {
        // building was destroyed
    }

    @Override
    public String toString() {
        return "state : destoryed";
    }
}
