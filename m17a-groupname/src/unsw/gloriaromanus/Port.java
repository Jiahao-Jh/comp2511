package unsw.gloriaromanus;

import java.io.Serializable;

public class Port implements Infrastructure, Production, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Province province;
    private int level;
    private int costTurns = 4;
    private int cost;
    private int upgradeFee;

    public Port(Province province) {
        this.province = province;
        level = 1;
    }

    public boolean upgradable() {
        return level < 3 && province.hasEnoughMoney(upgradeFee);
    }

    public void upgrade() {
        if (upgradable()){
            level++;
            province.getFaction().spend(upgradeFee);
        }
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
