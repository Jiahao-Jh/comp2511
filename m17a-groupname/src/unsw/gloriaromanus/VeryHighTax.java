package unsw.gloriaromanus;

import java.io.Serializable;

public class VeryHighTax implements TaxRate, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Province province;

    private final int wealthGrowth = -30;
    private final double rate = 0.25;

    public VeryHighTax(Province province) {
        this.province = province;
    }

    public int getTax() {
        province.increaseWealth(wealthGrowth);
        for (Soldier s : province.getSoldiers()) {
            s.setMorale(s.getMorale() - 1);
        }
        int income = (int) Math.round(province.getWealth() * rate);
        return income;
    }

    public void changeTaxRate(String rate) {
        switch (rate) {
            case "normal" :
                province.setTaxRate(province.getNormalTax());
                break;
            case "high" :
                province.setTaxRate(province.getHighTax());
                break;
            case "low" :
                province.setTaxRate(province.getLowTax());
                break;
            default :
                break;
                // do nothing
        }
    }

    public int getWealthGrow(){
        return wealthGrowth;
    }

    @Override
    public String toString() {
        return "Very High Tax Rate";
    }
}
