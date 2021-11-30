package unsw.gloriaromanus;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

public class Province implements ManufactureBuilding , Serializable{

    private static final long serialVersionUID = 1L;

    private Faction faction;

    private String name;

    private List<Soldier> soldiers;

    private int wealth;
    private LowTax lowTax;
    private NormalTax normalTax;
    private HighTax highTax;
    private VeryHighTax veryHighTax;
    private TaxRate taxRate;
    
    private Mine mine;
    private Farm farm;
    private Port port;
    private Market market;
    private Wall wall;
    private TroopProductionBuilding troopProductionBuilding;

    private Infrastructure constructing;
    private int remainTurns;

    private VacantState vacantState;
    private OccupiedState occupiedState;
    private RepairState repairState;
    private DestroyedState destroyedState;
    private State state;
    
    private final int repairCostTurns = 1;

    public Province(String name, Faction faction) {
        this.faction = faction;

        this.name = name;

        soldiers = new ArrayList<>();

        wealth = 200;
        lowTax = new LowTax(this);
        normalTax = new NormalTax(this);
        highTax = new HighTax(this);
        veryHighTax = new VeryHighTax(this);
        taxRate = normalTax;

        mine = null;
        farm = null;
        port = null;
        market = null;
        wall = null;
        troopProductionBuilding = null;

        constructing = null;
        remainTurns = 0;

        vacantState = new VacantState(this);
        occupiedState = new OccupiedState(this);
        repairState = new RepairState(this);
        destroyedState = new DestroyedState(this);
        state = vacantState;

    }
    
    /*
    Province only can construct infrastructure 
    If production is not infrastructure cancel it
    */
    public void setTask(Production production) {
        if (production == null) {
            constructing = null;
        } else if (production instanceof Infrastructure) {
            double finalCost = production.getCost();
            for(Province p : faction.getProvinces()) {
                Market m = p.getMarket();
                if (m != null) {
                    finalCost *= (1 - m.getRate());
                }
            }
            if (hasEnoughMoney((int) finalCost)) {
                int finalCostTurns = production.getCostTurns();;
                int mineBuff = 0;
                for (Province p : faction.getProvinces()) {
                    if (p.getMine() != null && p.getMine().getLevel() == 3) {
                        mineBuff++;
                    }
                }
                finalCostTurns -= mineBuff;
                if (finalCostTurns < 1) finalCostTurns = 1;
                remainTurns = finalCostTurns;
                constructing = (Infrastructure) production;
                faction.spend((int)finalCost);;
            } else {
                state.cancel();
            }
        } else {
            state.cancel();
        }
    }

    public int getRemainTurns() {
        return remainTurns;
    }

    public void start(Production production) {
        state.start(production);
    }

    public void repair() {
        state.repair();
    }

    public void cancel() {
        faction.addTreasury((int)(((Production)constructing).getCost()*0.3));
        state.cancel();
        remainTurns = 0;
    }

    public void destroyed() {
        state.destroyed();
    }

    public void setRepair() {
        remainTurns = repairCostTurns;
    }

    public void work() {
        if(remainTurns != 0) {
            remainTurns--;
            if (remainTurns == 0) {
                completeTask();
            }
        }
        if (troopProductionBuilding != null) {
            troopProductionBuilding.work();
        }
    }

    public void completeTask() {
    
        if (state.equals(occupiedState)) {
            if (constructing instanceof Farm) {
                farm = (Farm) constructing;
            } else if (constructing instanceof Port) {
                port = (Port) constructing;
            } else if (constructing instanceof Market) {
                market = (Market) constructing;
            } else if (constructing instanceof Wall) {
                wall = (Wall) constructing;
            } else if (constructing instanceof Mine) {
                mine = (Mine) constructing;
            } else if (constructing instanceof TroopProductionBuilding) {
                troopProductionBuilding = (TroopProductionBuilding) constructing;
            }
        }  
        state.complete();
    }

    @Override
    public void setState(State state) {
        this.state = state;

    }

    @Override
    public State getVacant() {
        return vacantState;
    }

    @Override
    public State getOccupied() {
        return occupiedState;
    }

    @Override
    public State getRepair() {
        return repairState;
    }

    @Override
    public State getDestroyed() {
        return destroyedState;
    }

    public int getWealth() {
        return wealth;
    }

    public void increaseWealth(int growth) {
        wealth += growth;
        if (wealth < 0) wealth = 0;
    }

    public TaxRate getLowTax() {
        return lowTax;
    }

    public TaxRate getNormalTax() {
        return normalTax;
    }

    public TaxRate getHighTax() {
        return highTax;
    }

    public TaxRate getVeryHighTax() {
        return veryHighTax;
    }

    public TaxRate getCurrRate() {
        return taxRate;
    }

    public void changeTaxRate(String rateName) {
        taxRate.changeTaxRate(rateName);
    }

    public void setTaxRate(TaxRate taxRate) {
        this.taxRate = taxRate;
    }

    public int collectTax() {
        return taxRate.getTax();
    }

    public List<Soldier> getSoldiers() {
        return soldiers;
    }

    public String getName() {
        return name;
    }

    public State getState() {
        return state;
    }

    public boolean hasEnoughMoney(int cost) {
        return faction.hasEnoughMoney(cost);
    }

    public Farm getFarm() {
        return farm;
    }

    public Port getPort() {
        return port;
    }

    public Wall getWall() {
        return wall;
    }

    public Market getMarket() {
        return market;
    }

    public Mine getMine() {
        return mine;
    }

    public Faction getFaction() {
        return faction;
    }

    public void addSoldier(Soldier soldier) {
        soldiers.add(soldier);
    }
    
    public void setSoldiers(List<Soldier> soldiers) {
        this.soldiers = soldiers;
    }

    public void setFaction(Faction faction){
        this.faction = faction;
    }

    public Infrastructure getConstructing() {
        return constructing;
    }

    public TroopProductionBuilding getTroopProductionBuilding() {
        return troopProductionBuilding;
    }

    public String getTrain() {
        if (troopProductionBuilding == null) {
            return "This province does not have a troop production building yet";
        }
        String message = "";
        int ct = 0;
        for (ProductionSpot s : troopProductionBuilding.getSpots()) {
            message += "Spot " + ct + " : " + s.getState().toString() + "\n"
                        + "Remain Turns : " + s.getRemainTurns() + "\n";
        }
        return message;
    }

    public String getInfra() {
        String m1 = "";
        String m2 = "";
        String p = "";
        String f = "";
        String t = "";
        if (this.market != null ) {
            m1 = "Market : " + Integer.toString(market.getlvl()) + "\n";
        }
        if (this.mine != null ) {
            m2 = "Mine : " + Integer.toString(mine.getlvl()) + "\n";
        }
        if (this.port != null ) {
            p = "Port : " + Integer.toString(port.getlvl()) + "\n";
        }
        if (this.farm != null ) {
            f = "Farm : " + Integer.toString(farm.getlvl()) + "\n";
        }
        if (this.troopProductionBuilding != null) {
            t = "Troop Production Building : completed\n";
        }
        String s = state.toString() + "\n";
        String r = "remain turns : " + remainTurns;
        
        return m1 + m2 + p + f + t + s + r;
    }

    @Override
    public String toString(){
        String info = "";
        info += "Name : " + name + "\n" 
        + "Tax Rate : " + taxRate.toString() + "\n"
        + "Wealth : " + Integer.toString(wealth) + "\n"; 
        return info; 
    }

    public int getNumofTroops() {
        int res = 0;
        for (Soldier soldier : soldiers) {
            res += soldier.getNumTroops();
        }
        return res;
    }

    public void destoryAllBuilding() {
        mine = null;
        farm = null;
        port = null;
        market = null;
        wall = null;
        troopProductionBuilding = null;

        constructing = null;
        remainTurns = 0;
        state = vacantState;
    }
}
