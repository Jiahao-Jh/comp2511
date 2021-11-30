package unsw.gloriaromanus;

import java.io.Serializable;

public class NormalTax implements TaxRate, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Province province;

    private final int wealthGrowth = 0;
    private final double rate = 0.15;

    public NormalTax(Province province) {
        this.province = province;
    }

    public int getTax() {
        province.increaseWealth(wealthGrowth);
        int income = (int) Math.round(province.getWealth() * rate);
        return income;
    }

    public void changeTaxRate(String rate) {
        switch (rate) {
            case "low" :
                province.setTaxRate(province.getLowTax());
                break;
            case "high" :
                province.setTaxRate(province.getHighTax());
                break;
            case "very high" :
                province.setTaxRate(province.getVeryHighTax());
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
        return "Normal Tax Rate";
    }
}
