package unsw.gloriaromanus;

import java.io.Serializable;

public class Mine implements Infrastructure, Production, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Province province;

    private int level;
    private int costTurns;
    private int cost;
    private int upgradeFee;

    public Mine(Province province) {
        this.province = province;
        costTurns = 1;
        cost = 80;
        level = 1;
        upgradeFee = 30;
    }

    public void upgrade() {
        if (upgradable()){
            level++;
            province.getFaction().spend(upgradeFee);
        }
    }

    public boolean upgradable() {
        return level < 3 && province.hasEnoughMoney(upgradeFee);
    }

    public double getRate() {
        return 0.05 * level;
    }

    public int getCostTurns() {
        return costTurns;
    }

    public int getLevel() {
        return level;
    }

    public int getCost() {
        return cost;
    }

    public int getlvl() {
        return level;
    }
}
