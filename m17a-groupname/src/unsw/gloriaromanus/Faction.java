package unsw.gloriaromanus;

import java.util.List;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Faction implements Serializable{

    private static final long serialVersionUID = 1L;
    private List<Province> provinces;
    private String name;
    private int treasury;
    private int wealth;
    private Graph g;

    public Faction(String name) {
        this.name = name;
        provinces = new ArrayList<>();
        g = new Graph();
        treasury = 300;
        wealth = 0;
    }

    public void addProvinces(Province province) {
        if (!provinces.contains(province)){
            provinces.add(province);
        }

    }

    public void constructGraph() {
        int size = provinces.size();
        LinkedList<Province> pList = new LinkedList<>();
        for (Province p : provinces) {
            pList.add(p);
        }
        try {
            g.constructGraph(pList, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Graph getGraph() {
        return g;
    }

    public void increaseTreasury() {
        for (Province p : provinces) {
            treasury += p.collectTax();
        }
    }

    public boolean hasEnoughMoney(int cost) {
        return cost <= treasury;
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public void spend(int price) {
        treasury -= price;
    }

    public void removeProvince(Province province) {
        provinces.remove(province);

    }

    public void addTreasury(int money) {
        treasury += money;
    }

    public String getName() {
        return name;
    }

    public int getTreasury() {
        return treasury;
    }

    public int getWealth() {
        for (Province province : provinces) {
            wealth += province.getWealth();
        }
        return wealth;
    }

    public void setTreasury(int treasury) {
        this.treasury = treasury;
    }

    public void setWealth(int wealth) {
        this.wealth = wealth;
    }
}
