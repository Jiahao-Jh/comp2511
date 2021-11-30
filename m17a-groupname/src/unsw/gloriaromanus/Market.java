package unsw.gloriaromanus;

import java.io.Serializable;

public class Market implements Infrastructure, Production, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Province province;

    private int level;
    private int costTurns = 2;
    private int cost = 200;
    private int upgradeFee;

    public Market(Province province) {
        this.province = province;
        level = 1;
    }

    public void upgrade() {
        if(upgradable()) {
            level++;
            province.getFaction().spend(upgradeFee);
        }
    }

    public double getRate() {
        return 0.1 * level;
    }

    public boolean upgradable() {
        return level < 3 && province.hasEnoughMoney(upgradeFee);
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
