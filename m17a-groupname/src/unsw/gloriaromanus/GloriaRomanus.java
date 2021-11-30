package unsw.gloriaromanus;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import org.json.JSONArray;
import org.json.JSONObject;

public class GloriaRomanus implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Faction> factions;
    private LinkedList<Province> provinces;
    private Map<String, String> provinceToOwningFactionMap;

    int treasury;
    int conquest;
    int wealth;

    private int ctTurns;
    private int numFactions;
    private Faction currFaction;
    private Graph MapGraph;

    public GloriaRomanus(int numOfPlayers) throws IOException {
        ctTurns = 0;
        treasury = 0;
        conquest = 0;
        wealth = 0;
        factions = new ArrayList<>();

        LinkedList<Province> provinces = new LinkedList<>();
        String content = null;
        if(numOfPlayers == 2){
            content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
        } else if (numOfPlayers == 3){
            content = Files.readString(Paths.get("src/unsw/gloriaromanus/3_player_provinces.json"));
        }else if (numOfPlayers == 4){
            content = Files.readString(Paths.get("src/unsw/gloriaromanus/4_player_provinces.json"));
        }
        JSONObject ownership = new JSONObject(content);
        Map<String, String> m = new HashMap<String, String>();
        for (String factionName : ownership.keySet()) {
            // loop through factions
            Faction f = new Faction(factionName);
            factions.add(f);
            
            JSONArray ja = ownership.getJSONArray(factionName);
            for (int i = 0; i < ja.length(); i++) {
                // loop through provinces in a faction
                String provinceName = ja.getString(i);
                Province p = new Province(provinceName, f);
                f.addProvinces(p);
                provinces.add(p);
                m.put(provinceName, factionName);
            }
            f.constructGraph();
        }
        MapGraph = new Graph();
        MapGraph.constructGraph(provinces, provinces.size());

        provinceToOwningFactionMap = m;
        numFactions = factions.size();
        currFaction = factions.get(0);
        this.provinces = provinces;
    }

    public void performAction(String action) {
        switch (action){
            case "end" : 
                endTurn();
                break;
            default :
                break;
        }
        checkVictory();
    }

    public void endTurn() {
        int factionIndex = ++ctTurns % numFactions;
        currFaction = factions.get(factionIndex);
        for (Province p : currFaction.getProvinces()) {
            p.work();
            for (Soldier s : p.getSoldiers()) {
                s.resetMovementPoints();
            }
        }
        currFaction.increaseTreasury();

    }

    public boolean isConnected(String p1, String p2) {
        int index1 = MapGraph.getIndex(p1);
        int index2 = MapGraph.getIndex(p2);
        if (index1 != -1 && index2 != -1) {
            return MapGraph.getBean(index1, index2);
        }
        return false;
    }

    public Map<String, String> getProvinceOwning() {
        return provinceToOwningFactionMap;
    }

    public Faction getFaction(String faction_name) {
        for (Faction faction : factions) {
            if(faction.getName().equals(faction_name)){
                return faction;
            }
        }
        return null;
    }

    public List<Faction> getFactions() {
        return factions;
    }

    public void setCurrFaction(Faction f) {
        currFaction = f;
    }

    public Faction getCurrFaction() {
        return currFaction;
    }

    // only implement and
    public void setGoal(String input) {
        // input = "TREASURY and CONQUEST and WEALTH"
        if (input.contains("TREASURY")){
            treasury = 1;
        } 
        if (input.contains("CONQUEST")){
            conquest = 1;
        } 
        if (input.contains("WEALTH")){
            wealth = 1;
        }
    }

    public int getTreasuryGoal() {
        return treasury;
    }

    public String getInfo(String province) {
        Province selectedProvince = getProvince(province);
        if (selectedProvince != null) {
            return selectedProvince.toString();
        } else {
            return "This province is not owned by current player";
        }
    } 

    public Province getProvince(String name) {
        for (Province p : currFaction.getProvinces()) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public Province getProvinceOfAllFaction(String name) {
        for (Faction faction : factions) {
            for (Province p : faction.getProvinces()) {
                if (p.getName().equals(name)) {
                    return p;
                }
            }
        }
        return null;
    }

    public Province getFromAllProvince(String name) {
        for (Province p : provinces) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public boolean checkVictory() {
        int flag = 0;
        if (treasury == 1){
            if (currFaction.getTreasury() >= 100000){
                flag++;
            }
        }
        if (conquest == 1){
            if (currFaction.getProvinces().size() == 53){
                
                flag++;
            }
        }
        if (wealth == 1){
            if (currFaction.getWealth() >= 400000){
                flag++;
            }
        }
        if (flag == (wealth + conquest + treasury) && flag >0){
            return true;
        }
        return false;
    }

    public void build(String name, String production) {
        Province p = getProvince(name);
        Production pro;
        switch (production) {
            case "farm":
                pro = new Farm(p);
                break;
            case "port":
                pro = new Port(p);
                break;
            case "market":
                pro = new Market(p);
                break;
            case "mine":
                pro = new Mine(p);
                break;
            case "train":
                pro = new TroopProductionBuilding(p);
                break;
            default:
                pro = null;
                break;
        }
        if (pro != null) {
            p.start(pro);
        }
    }

    public void upgrade(String name, String production) {
        Province p = getProvince(name);
        Infrastructure pro;
        switch (production) {
            case "farm":
                pro = p.getFarm();
                break;
            case "port":
                pro = p.getPort();
                break;
            case "market":
                pro = p.getMarket();
                break;
            case "mine":
                pro = p.getMine();
                break;
            default:
                pro = null;
                break;
        }
        if (pro != null) {
            pro.upgrade();
        }
    }

    public void train(String province, String type) throws IOException {
        Province p = getProvince(province);
        if (p.getTroopProductionBuilding() == null) {
            return;
        }
        Soldier s;
        switch (type) {
            case "artillery":
                s = new Artillery(p);
                break;
            case "berserker":
                s = new Berserker(p);
                break;
            case "chariot":
                s = new Chariot(p);
                break;
            case "druid":
                s = new Druid(p);
                break;
            case "elephants":
                s = new Elephants(p);
                break;
            case "horse archer":
                s = new HorseArcher(p);
                break;
            case "javelin skirmisher":
                s = new JavelinSkirmisher(p);
                break;
            case "melee cavalry":
                s = new MeleeCavalry(p);
                break;
            case "missile infantry":
                s = new MissileInfantry(p);
                break;
            case "pikemen":
                s = new Pikemen(p);
                break;
            case "roman legionary":
                s = new RomanLegionary(p);
                break;
            case "spearmen":
                s = new Spearmen(p);
                break;
            default:
                s = null;
                break;
        }
        if (s != null) {
            p.getTroopProductionBuilding().start(s);
        }
    }

    public String getInfra(String province) {
        Province selectedProvince = getProvince(province);
        if (selectedProvince != null) {
            return selectedProvince.getInfra();
        } else {
            return "This province is not owned by current player";
        }
    }

    public String getTrain(String province) {
        Province selectedProvince = getProvince(province);
        if (selectedProvince != null) {
            return selectedProvince.getTrain();
        } else {
            return "This province is not owned by current player";
        }
    }
}
