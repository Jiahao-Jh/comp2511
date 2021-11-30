package unsw.gloriaromanus;

import java.io.Serializable;

public class Wall implements Infrastructure, Production, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Province province;

    private int level;
    private int costTurns = 3;
    private int cost;
    private int upgradeFee;

    public Wall() {
        level = 1;
    }

    public void upgrade() {
        level++;

    }

    public boolean upgradable() {
        return level < 3 && province.hasEnoughMoney(upgradeFee);
    }

    public double getRate() {
        return 0.02 * level;
    }

    public int getCostTurns() {
        return costTurns;
    }

    public int getCost() {
        return cost;
    }
}
