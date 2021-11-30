package unsw.gloriaromanus;

public interface ManufactureBuilding {
    void setState(State state);
    // set task
    void setTask(Production production);
    void setRepair();
    // get corresponding State
    State getVacant();
    State getOccupied();
    State getRepair();
    State getDestroyed();
    // reduce remainTurns by 1
    void work();
    // add constructing infrastructure to corresponding place
    void completeTask();
}
