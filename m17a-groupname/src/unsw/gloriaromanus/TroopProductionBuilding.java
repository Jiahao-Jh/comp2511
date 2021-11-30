package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TroopProductionBuilding implements Infrastructure, Production, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Province province;

    private List<ProductionSpot> spots;

    private int costTurn;
    private int cost;
    private int upgradeFee;
    private int level;
    private int remainTurns;
    
    public TroopProductionBuilding(Province province) {
        costTurn = 1;
        cost = 100;
        remainTurns = 0;
        level = 1;
        this.province = province;
        spots = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ProductionSpot p = new ProductionSpot(province);
            spots.add(p);
        }

    }


    public void start(Production production) {
        if (!(production instanceof Soldier)) {
            return;
        }
        for (ProductionSpot p : spots) {
            if (p.getState().equals(p.getVacant())) {
                p.start(production);
                break;
            }
        }
    }

    public void cancel(int index) {
        spots.get(index).cancel();
    }

    public void repair() {
        for (ProductionSpot p : spots) {
            p.repair();
        }
    }

    public void destroyed() {
        for (ProductionSpot p : spots) {
            p.destroyed();
        }
    }

    public int getCostTurns() {
        return costTurn;
    }

    public int getCost() {
        return cost;
    }

    public List<ProductionSpot> getSpots() {
        return spots;
    }

    public void upgrade() {
        if (upgradable()) {
            level++;
            province.getFaction().spend(upgradeFee);
        }     
    }

    public boolean upgradable() {
        return level < 3 && province.hasEnoughMoney(upgradeFee);
    }

    public void work() {
        if (remainTurns != 0) {
            remainTurns --;
            if (remainTurns == 0) {
                for (ProductionSpot s : spots) {
                    s.setState(s.getVacant());;
                }
            }
        } else if (remainTurns == 0) {
            for (ProductionSpot s : spots) {
                s.work();
            }
        }
    }
}
