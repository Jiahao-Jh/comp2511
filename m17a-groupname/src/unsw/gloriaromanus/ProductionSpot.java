package unsw.gloriaromanus;

import java.io.Serializable;

public class ProductionSpot implements ManufactureBuilding, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Province province;

    private VacantState vacantState;
    private OccupiedState occupiedState;
    private RepairState repairState;
    private DestroyedState destroyedState;
    private State state;

    private Soldier trainning;
    private int remainTurns;
    private int repairCost;

    public ProductionSpot(Province province) {
        this.province = province;
        vacantState = new VacantState(this);
        occupiedState = new OccupiedState(this);
        repairState = new RepairState(this);
        destroyedState = new DestroyedState(this);
        
        state = vacantState;
        repairCost = 1;

        trainning = null;
        remainTurns = 0;
    }

    public void start(Production production) {
        state.start(production);
    }

    public void repair() {
        state.repair();
        remainTurns = repairCost;
    }

    public void destroyed() {
        state.destroyed();
    }

    public void cancel() {
        province.getFaction().addTreasury((int)(trainning.getCost()*0.3));
        state.cancel();
        remainTurns = 0;
    }

    public State getVacant() {
        return vacantState;
    }

    public State getOccupied() {
        return occupiedState;
    }
    public State getRepair() {
        return repairState;
    }
    
    public State getDestroyed() {
        return destroyedState;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void setTask(Production production) {
        if (production == null) {
            trainning = null;
            return;
        }
        double finalCost = production.getCost();
        for (Province p : province.getFaction().getProvinces()) {
            Mine m = p.getMine();
            if (m != null) {
                finalCost *= (1 - m.getRate());
            }                
        }
        if (province.hasEnoughMoney((int)finalCost)) {
            double finalCostTurns = production.getCostTurns();
            for (Province p : province.getFaction().getProvinces()) {
                Farm f = p.getFarm();
                if (f != null) {
                    finalCostTurns *= (1 - f.getRate());
                }
            }
            if (finalCostTurns < 1) finalCostTurns = 1;
            remainTurns = (int) finalCostTurns;
            trainning = (Soldier) production;
            province.getFaction().spend((int)finalCost);
        } else {
            state.cancel();
        }
    }

    public Soldier getTrainning() {
        return trainning;
    }

    @Override
    public void setRepair() {  
    }

    @Override
    public void work() {
        if (state.equals(occupiedState) || state.equals(repairState)) {
            remainTurns--;
            if (remainTurns == 0) {
                completeTask();
            }
        }
    }

    @Override
    public void completeTask() {
        if (state.equals(occupiedState)) {
            province.getSoldiers().add(trainning);
        }
        state.complete();
    }

    public boolean hasEnoughMoney(int cost) {
        return province.getFaction().hasEnoughMoney(cost);
    }

    public int getRemainTurns() {
        return remainTurns;
    }

}
