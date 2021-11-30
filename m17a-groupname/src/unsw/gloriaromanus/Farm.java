package unsw.gloriaromanus;

import java.io.Serializable;

public class Farm implements Infrastructure, Production, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Province province;

    private int level;
    private int costTurns;
    private int cost;
    private int upgradeFee;

    public Farm(Province province) {
        this.province = province;
        upgradeFee = 30;
        cost = 60;
        costTurns = 1;
        level = 1;
    }

    public void upgrade() {
        if (upgradable()) {
            level ++;
            province.getFaction().spend(upgradeFee);
        }
    }

    public boolean upgradable() {
        return level < 3 && province.hasEnoughMoney(upgradeFee);
    }

    public double getRate() {
        return 0.2 * level;
    }

    public int getCostTurns() {
        return costTurns;
    }

    public int getCost() {
        return cost;
    }

    public int getlvl() {
        return level;
    }
}
