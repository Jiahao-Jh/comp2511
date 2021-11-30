package unsw.gloriaromanus;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class Engagements implements Battle_subject {

    private List<Battle_observer> Battle_observers;

    private Soldier my_unit;
    private Soldier enemy_unit;

    private List<Soldier> my_soldiers;
    private List<Soldier> enemy_soldiers;

    private double my_morale;
    private int my_melee_attack_damage;
    private int my_armour;
    private int my_charge;
    private int my_defenseSkill;
    private double my_speed;
    private int my_shieldDefense;
    private double my_ranged_attack;
    private int my_melee_infantry_count = 0;



    private double enemy_morale;
    private int enemy_melee_attack_damage;
    private int enemy_armour;
    private int enemy_charge;
    private int enemy_defenseSkill;
    private double enemy_speed;
    private int enemy_shieldDefense;
    private double enemy_ranged_attack;
    private int enemy_melee_infantry_count = 0;


    private Integer engagement_count;
    private boolean isRanged;
    static final double BASEFLEECHANCE = 0.5;
    static final double MINFLEECHANCE = 0.1;
    static final double MAXFLEECHANCE = 1;

    public Engagements(Soldier my_unit, Soldier enemy_unit,List<Soldier> my_soldiers, List<Soldier> enemy_soldiers,  boolean flag) {
        this.my_unit = my_unit;
        this.enemy_unit = enemy_unit;
        this.my_soldiers = my_soldiers;
        this.enemy_soldiers = enemy_soldiers;
        this.isRanged = flag;
        engagement_count = 0;

        // set battle static variable = 0
        my_morale = my_unit.getMorale();
        my_melee_attack_damage = my_unit.getAttack();
        my_armour = my_unit.getArmour();
        my_charge = my_unit.getCharge();
        my_shieldDefense = my_unit.getShieldDefense();
        my_defenseSkill = my_unit.getDefenseSkill();
        my_speed = my_unit.getSpeed();
        my_ranged_attack = my_unit.getRangedAttack();

        enemy_morale = enemy_unit.getMorale();
        enemy_melee_attack_damage = enemy_unit.getAttack();
        enemy_armour = enemy_unit.getArmour();
        enemy_charge = enemy_unit.getCharge();
        enemy_shieldDefense = enemy_unit.getShieldDefense();
        enemy_defenseSkill = enemy_unit.getDefenseSkill();
        enemy_speed = enemy_unit.getSpeed();
        enemy_ranged_attack = enemy_unit.getRangedAttack();

        Battle_observers = new ArrayList<Battle_observer>();
    }

    public int run(){
        int my_unit_start_size = my_unit.getNumTroops();
        int enemy_unit_start_size = enemy_unit.getNumTroops();
        Random r = new Random();

        if (isRanged){
            notifyObservers("Ranged engagements between " + my_unit.getClass().getSimpleName()
            + " " + enemy_unit.getClass().getSimpleName());
        } else{
            notifyObservers("Melee engagements between " + my_unit.getClass().getSimpleName()
            + " " + enemy_unit.getClass().getSimpleName());
        }
        // TODO Berserker special ability and bonuses

        

        applySpecialAbility(my_soldiers,my_unit,true);
        applySpecialAbility(enemy_soldiers,enemy_unit,false);





        
        while (engagement_count < 200){
            double casualties_of_enemy = 0;
            //handle special ability and ranged engagement 
            if (enemy_armour == 0 || enemy_shieldDefense == 0){
                casualties_of_enemy = (enemy_unit_start_size*0.1)*10 * (r.nextGaussian()+1);
            } else if (isRanged){
                //spearmen speacial abailty
                if (my_unit.getClass() == Spearmen.class){
                    casualties_of_enemy = (enemy_unit_start_size*0.1)*(my_unit.getRangedAttack()/
                    ((double)enemy_armour / 2 + enemy_shieldDefense)) * (r.nextGaussian()+1);
                } else{
                    casualties_of_enemy = (enemy_unit_start_size*0.1)*(my_unit.getRangedAttack()/
                    (enemy_armour + enemy_shieldDefense)) * (r.nextGaussian()+1);
                }
            } else {
                casualties_of_enemy = (enemy_unit_start_size*0.1)*(my_melee_attack_damage/
                (enemy_armour + enemy_shieldDefense + enemy_defenseSkill)) * (r.nextGaussian()+1);
            }
            casualties_of_enemy = Math.round(casualties_of_enemy);

            if (casualties_of_enemy > enemy_unit.getNumTroops()){
                casualties_of_enemy = enemy_unit.getNumTroops();
            }

            double casualties_of_my = 0;
            //handle special ability and ranged engagement 
            if (my_armour == 0 || my_shieldDefense == 0){
                casualties_of_my = (my_unit_start_size*0.1)*10 * (r.nextGaussian()+1);
            } else if (isRanged){
                //spearmen speacial abailty
                if (enemy_unit.getClass() == Spearmen.class){
                    casualties_of_my = (my_unit_start_size*0.1)*(enemy_unit.getRangedAttack()/
                    ((double)my_armour/2 + my_shieldDefense) ) * (r.nextGaussian()+1);
                } else {
                    casualties_of_my = (my_unit_start_size*0.1)*(enemy_unit.getRangedAttack()/
                    (my_armour + my_shieldDefense) ) * (r.nextGaussian()+1);
                }
            }else{
                casualties_of_my = (my_unit_start_size*0.1)*(enemy_melee_attack_damage/
                (my_armour + my_shieldDefense + my_defenseSkill)) * (r.nextGaussian()+1);
            }
            casualties_of_my = Math.round(casualties_of_my);

            if (casualties_of_my > my_unit.getNumTroops()){
                casualties_of_my = my_unit.getNumTroops();
            }


            //elephant Special Ability
            int elephant_flag = 0;
            Soldier enemy_initial_unit = enemy_unit;
            Soldier my_initial_unit = my_unit;

            if (my_unit.getClass() == Elephants.class){
                if (r.nextDouble() < 0.1){
                    //find a random unit except current unit
                    while (enemy_soldiers.size() > 1 && enemy_unit.getClass() == enemy_initial_unit.getClass()){
                        elephant_flag = 1;
                        enemy_unit = enemy_soldiers.get(r.nextInt(enemy_soldiers.size()));
                    }

                }
            } 
            if (enemy_unit.getClass() == Elephants.class){
                if (r.nextDouble() < 0.1){
                    while (my_soldiers.size() > 1 && my_unit.getClass() == my_initial_unit.getClass()){
                        elephant_flag = 2;
                        my_unit = my_soldiers.get(r.nextInt(my_soldiers.size()));
                    }
                }
            }

            // melee infantry Special Ability
            int melee_infantry_flag = 0;
            if (my_unit.getClass() == HeavyInfantry.class){
                my_melee_infantry_count++;
                if (my_melee_infantry_count % 4 == 0){
                    my_melee_attack_damage += my_shieldDefense;
                    melee_infantry_flag = 1;
                }
            } 
            if (enemy_unit.getClass() == HeavyInfantry.class){
                enemy_melee_infantry_count++;
                if (enemy_melee_infantry_count % 4 == 0){
                    enemy_melee_attack_damage += enemy_shieldDefense;
                    melee_infantry_flag = 2;
                }
            }





            inflictCasualties(my_unit,(int)casualties_of_my);

            inflictCasualties(enemy_unit,(int)casualties_of_enemy);

            if(melee_infantry_flag ==1){
                my_melee_attack_damage -= my_shieldDefense;
            }
            if(melee_infantry_flag ==2){
                enemy_melee_attack_damage -= enemy_shieldDefense;
            }

            if (elephant_flag == 1){
                enemy_unit = enemy_initial_unit;
            } 
            if (elephant_flag == 2){
                my_unit = my_initial_unit;
            }


            //unit got destoryed
            if (isUnitEmpty(my_unit) && !isUnitEmpty(enemy_unit)){
                notifyObservers("your unit is destroyed , ENEMY GOT THIS ONE");
                return 2;
            }
            if(isUnitEmpty(enemy_unit) && !isUnitEmpty(my_unit)){
                notifyObservers("enemy unit is destroyed , YOU GOT THIS ONE");
                return 1;
            }
            if(isUnitEmpty(enemy_unit) && isUnitEmpty(my_unit)){
                notifyObservers("both unit is destroyed , it is a draw");
                return 0;
            }

            //calculate breaking 
            double my_breaking_prob = 0;
            if (Double.isInfinite(my_morale)){
                my_breaking_prob = 0;
            } else {
                my_breaking_prob = 1-(my_morale*0.1);
                my_breaking_prob += (casualties_of_my/my_unit_start_size)/(casualties_of_enemy/enemy_unit_start_size)*0.1;
            }
            double my_breaking = r.nextDouble();




            double enemy_breaking_prob = 0;
            if (Double.isInfinite(enemy_morale)){
                my_breaking_prob = 0;
            } else {
                enemy_breaking_prob = 1-(enemy_morale*0.1);
                enemy_breaking_prob += (casualties_of_enemy/enemy_unit_start_size)/(casualties_of_my/my_unit_start_size)*0.1;
            }
            double enemy_breaking = r.nextDouble();


            // both breaking at same time
            if (enemy_breaking < enemy_breaking_prob && my_breaking < my_breaking_prob){
                
                notifyObservers("both units breaks, it is a draw");
                return 0;

            // enemy units breaks
            } else if (enemy_breaking < enemy_breaking_prob && my_breaking >= my_breaking_prob){
                notifyObservers("enemy units breaks");
                double enemy_route_chance = BASEFLEECHANCE + 0.1 * (enemy_speed - my_speed);
                // while fail to route, keep suffer damage
                while (r.nextDouble() < enemy_route_chance && !isUnitEmpty(enemy_unit)){
                    inflictCasualties(enemy_unit,(int)casualties_of_enemy);
                }
                if (!isUnitEmpty(enemy_unit)){
                    notifyObservers("enemy unit successful route");
                } else {
                    notifyObservers("enemy unit failed to route");
                }
                return 1;

            // my units breaks
            } else if (enemy_breaking >= enemy_breaking_prob && my_breaking < my_breaking_prob){
                notifyObservers("your units breaks");
                double my_route_chance = BASEFLEECHANCE + 0.1 * (my_speed - enemy_speed);
                while (r.nextDouble() < my_route_chance && !isUnitEmpty(my_unit)){
                    inflictCasualties(my_unit,(int)casualties_of_my);
                }
                if (!isUnitEmpty(my_unit)){
                    notifyObservers("your unit successful route");
                } else {
                    notifyObservers("your unit failed to route");
                }


                return 2;
            }

            engagement_count++;

        }
       
        return 3;

    }

    public void applySpecialAbility(List<Soldier> soldiers, Soldier unit, boolean isMySoldier) {
        boolean RomanLegionaryflag = false;

        for (Soldier soldier : soldiers) {
            //RomanLegionary
            if (soldier.getClass() == RomanLegionary.class && RomanLegionaryflag == false){
                if (isMySoldier){
                    my_morale++;
                } else{
                    enemy_morale++;
                }
                RomanLegionaryflag = true;
            }


        }

        //druid
        boolean Druidflag = false;
        int buff_apply_times = 0;
        for (Soldier soldier : soldiers) {
            if (soldier.getClass() == Druid.class && Druidflag == false){
                if (isMySoldier){
                    buff_apply_times = soldier.getNumTroops();
                    if (buff_apply_times > 5){
                        buff_apply_times = 5;
                    }
                    my_morale = my_morale * (0.1*buff_apply_times);
                    enemy_morale = enemy_morale * (1 - (0.05 * buff_apply_times));


                } else{
                    buff_apply_times = soldier.getNumTroops();
                    if (buff_apply_times > 5){
                        buff_apply_times = 5;
                    }
                    enemy_morale = enemy_morale * (0.1*buff_apply_times);
                    my_morale = my_morale * (1 - (0.05 * buff_apply_times));

                }
                Druidflag = true;
            }


        }



        //Berserker 
        if (unit.getClass() == Berserker.class ){
            if (isMySoldier){
                my_morale = Double.POSITIVE_INFINITY;
                my_melee_attack_damage = my_melee_attack_damage * 2;
                my_armour = 0;
                my_shieldDefense = 0;
            } else{
                enemy_morale = Double.POSITIVE_INFINITY;
                enemy_melee_attack_damage = enemy_melee_attack_damage * 2;
                enemy_armour = 0;
                enemy_shieldDefense = 0;
            }

        }

        //melee cavalry
        if (unit.getClass() == MeleeCavalry.class ){
            double my_unit_num = (double)my_unit.getNumTroops() / 2;
            if (enemy_unit.getNumTroops() < my_unit_num){
                if (isMySoldier){
                    my_charge = my_charge * 2;
                    my_morale = my_morale * 1.5;
                } else{
                    enemy_charge = enemy_charge * 2;
                    enemy_morale = enemy_morale * 1.5;
                }
            }

        }

        //pikemen
        if (unit.getClass() == Pikemen.class ){
            if (isMySoldier){
                my_defenseSkill= my_defenseSkill * 2;
                my_speed = my_speed * 0.5;
            } else{
                enemy_defenseSkill= enemy_defenseSkill * 2;
                enemy_speed = enemy_speed * 0.5;
            }

        }

        //horse-archer
        if (unit.getClass() == HorseArcher.class ){
            if (isMySoldier){
                if(enemy_unit.getClass() == MissileInfantry.class){
                    enemy_ranged_attack = enemy_ranged_attack/2;
                }
            } else{
                if(my_unit.getClass() == MissileInfantry.class){
                    my_ranged_attack = my_ranged_attack/2;
                }
            }

        }





        //apply charge value
        if (unit.getClass() == MeleeCavalry.class || unit.getClass() == Chariot.class || unit.getClass() == Elephants.class){

            if (isMySoldier){
                my_melee_attack_damage = my_charge + my_melee_attack_damage;
            } else{
                enemy_melee_attack_damage = enemy_charge + enemy_melee_attack_damage;
            }

        }

    }

    public boolean isUnitEmpty(Soldier soldier) {
        if (soldier.getNumTroops() > 0){
            return false;
        }
        return true;
    }

    public void inflictCasualties(Soldier unit, int casualties){
        int num_of_unit = unit.getNumTroops();
        unit.setNumTroops(num_of_unit - casualties);

    }





    @Override
    public void attach(Battle_observer o) {
        if(! Battle_observers.contains(o)){
             Battle_observers.add(o);
        }
    }

    @Override
    public void detach(Battle_observer o) {
        Battle_observers.remove(o);
        
    }

    @Override
    public void notifyObservers(String message) {
		for( Battle_observer o : Battle_observers) {
			o.update(message);
		}
        
    }
	public void setEngagementCount(Integer input ) {
		this.engagement_count = input;
	}

	public Integer getEngagementCount() {
		return engagement_count;
    }

    
}
