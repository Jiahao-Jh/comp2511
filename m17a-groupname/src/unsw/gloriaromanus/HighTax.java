package unsw.gloriaromanus;

import java.io.Serializable;

public class HighTax implements TaxRate, Serializable {

    private static final long serialVersionUID = 1L;

    private Province province;

    private final int wealthGrowth = -10;
    private final double rate = 0.20;

    public HighTax(Province province) {
        this.province = province;
    }

    public int getTax() {
        province.increaseWealth(wealthGrowth);
        int income = (int) Math.round(province.getWealth() * rate);
        return income;
    }

    public void changeTaxRate(String rate) {
        switch (rate) {
            case "normal" :
                province.setTaxRate(province.getNormalTax());
                break;
            case "low" :
                province.setTaxRate(province.getLowTax());
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
        return "High Tax Rate";
    }
}
