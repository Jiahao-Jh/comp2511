package unsw.gloriaromanus;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

public abstract class Soldier implements Production, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Province province;

    private int numTroops;  // the number of troops in this unit (should reduce based on depletion)
    private int range;  // range of the unit
    private int armour;  // armour defense
    private int morale;  // resistance to fleeing
    private int speed;  // ability to disengage from disadvantageous battle
    private int attack;  // can be either missile or melee attack to simplify. Could improve implementation by differentiating!
    private int defenseSkill;  // skill to defend in battle. Does not protect from arrows!
    private int shieldDefense; // a shield
    private int rangedAttack;
    private int charge; //charge value
    private int movementPoints;
    private int remainPoints;
    private int costTurns;
    private int cost;
    private String type;

    public Soldier(Province province, String filename) throws IOException {
        this.province = province;
        String content = Files.readString(Paths.get(filename));
        JSONObject j = new JSONObject(content);
        numTroops = j.getInt("numTroops");
        range = j.getInt("range");
        armour = j.getInt("armour");
        morale = j.getInt("morale");
        speed = j.getInt("speed");
        attack = j.getInt("attack");
        charge = j.getInt("charge");
        defenseSkill = j.getInt("defenseSkill");
        shieldDefense = j.getInt("shieldDefense");
        rangedAttack = j.getInt("rangedAttack");
        cost = j.getInt("cost");
        costTurns = j.getInt("costTurns");
        type = j.getString("type");
        movementPoints = j.getInt("mp");
        remainPoints = movementPoints;
    }
        

    public void addMorale(int buff) {
        morale += buff;
    }

    public void minusMorale(int buff) {
        morale -= buff;
        if (morale == 0) {
            morale = 1;
        }
    }

    public int getCharge() {
        return charge;
    }

    public int getMorale() {
        return morale;
    }

    public int getRange() {
        return range;
    }

    public int getArmour() {
        return armour;
    }

    public int getSpeed() {
        return speed;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefenseSkill() {
        return defenseSkill;
    }

    public int getShieldDefense() {
        return shieldDefense;
    }

    public int getNumTroops() {
        return numTroops;
    }

    public void setNumTroops(int num) {
        numTroops = num;
    }

    public void move(Province province) {
        if (province.getFaction().equals(this.province.getFaction())) {
            int mpCost = province.getFaction().getGraph().reachable(this.province, province);
            if (mpCost != 0 && remainPoints >= mpCost) {
                this.province.getSoldiers().remove(this);
                province.getSoldiers().add(this);
                this.province = province;
                remainPoints -= mpCost;
            } else {
                // not enough movement points
            }
        }
    }

    public void resetMovementPoints() {
        remainPoints = movementPoints;
    }

    public int getCostTurns() {
        return costTurns;
    }

    public int getCost() {
        return cost;
    }

    public int getRangedAttack() {
        return rangedAttack;
    }

    public void setArmour(int armour) {
        this.armour = armour;
    }

    public void setMorale(int morale) {
        this.morale = morale;
    }

    public Province getProvince() {
        return province;
    }

    public void setRemainMp(int mp) {
        remainPoints = mp;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        String s = "Troop Number : " + getNumTroops() + "\n"
                + " Type : " + getType() + "\n";
        return s;
    }

	public String getImageFileName() {
        String res = null;
        switch (type) {
            case "Horse Archer":
                res = "images/CS2511Sprites/Horse/Horse_Archer/Horse_Archer.png";
                break;
            case "Artillery":
                res = "images/CS2511Sprites/Cannon/Cannon.png";
                break;
            case "berserker":
                res = "images/CS2511Sprites/Swordsman/Swordsman.png";
                break;
            case "chariot":
                res = "images/CS2511Sprites/Chariot/Chariot.png";
                break;
            case "druid":
                res = "images/CS2511Sprites/Druid/Celtic_Druid.png";
                break;
            case "Elephant":
                res = "images/CS2511Sprites/Elephant/Alone/Elephant_Alone (1).png";
                break;     
            case "heavy infantry":
                res = "images/CS2511Sprites/Hoplite/Hoplite.png";
                break;
            case "Melee Cavalry":
                res = "images/CS2511Sprites/Horse/Horse_Heavy_Cavalry/Horse_Heavy_Cavalry.png";
                break;
            case "Missile Infantry":
                res = "images/CS2511Sprites/Trebuchet/Trebuchet.png";
                break;

            case "Pikemen":
                res = "images/CS2511Sprites/Pikeman/Pikeman.png";
                break;

            case "Roman Legionary":
                res = "images/legionary.png";
                break;

            case "Spearmen":
                res = "images/CS2511Sprites/Spearman/Spearman.png";
                break;
        }
        return res;
	}
}


