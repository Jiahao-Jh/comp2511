package unsw.gloriaromanus;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Battle_resolver implements Battle_subject{

    private Province my_province;
    private Province enemy_province;
    private List<Battle_observer> Battle_observers;

    private List<Soldier> my_soldiers;
    private List<Soldier> enemy_soldiers;
    private Integer engagement_count;

    private List<Soldier> my_routes_soldiers;
    private List<Soldier> enemy_routes_soldiers;

    //my_province = invander province
    //enemy_province = defender province
    public Battle_resolver(Province my_province ,Province enemy_province){
        this.my_province = my_province;
        this.enemy_province = enemy_province;
        this.engagement_count = 0;
        my_soldiers = my_province.getSoldiers();
        enemy_soldiers = enemy_province.getSoldiers();
        Battle_observers = new ArrayList<Battle_observer>();
        my_routes_soldiers = new ArrayList<Soldier>();
        enemy_routes_soldiers = new ArrayList<Soldier>();
    }


    public void startSkirmish() {
        Random r = new Random();

        while (!my_soldiers.isEmpty() && !enemy_soldiers.isEmpty() && engagement_count < 200 ){
            Soldier random_my_soldier = my_soldiers.get(r.nextInt(my_soldiers.size()));
            Soldier random_enemy_soldier = enemy_soldiers.get(r.nextInt(my_soldiers.size()));
            if ( isRanged(random_enemy_soldier.getClass()) && isRanged(random_my_soldier.getClass()) ){

                rangedEngagements(random_my_soldier, random_enemy_soldier);

            } else if ( !isRanged(random_enemy_soldier.getClass()) && !isRanged(random_my_soldier.getClass()) ){

                meleeEngagements(random_my_soldier, random_enemy_soldier);

            } else{
                double chance = 0.5;
                if (random_enemy_soldier.getClass() == MissileInfantry.class){
                    chance = chance + 0.1*(random_my_soldier.getSpeed()-random_enemy_soldier.getSpeed());
                    if (chance > 0.95){
                        chance = 0.95;
                    }
                    if (r.nextDouble() < chance){
                        meleeEngagements(random_my_soldier, random_enemy_soldier);
                    } else {
                        rangedEngagements(random_my_soldier, random_enemy_soldier);
                    }


                } else if (random_my_soldier.getClass() == MissileInfantry.class){
                    chance = chance + 0.1*(random_enemy_soldier.getSpeed()-random_my_soldier.getSpeed());
                    if (chance > 0.95){
                        chance = 0.95;
                    }
                    if (r.nextDouble() < chance){
                        meleeEngagements(random_my_soldier, random_enemy_soldier);
                    } else {
                        rangedEngagements(random_my_soldier, random_enemy_soldier);
                    }

                } else {
                    if (r.nextDouble() < chance){
                        meleeEngagements(random_my_soldier, random_enemy_soldier);
                    } else {
                        rangedEngagements(random_my_soldier, random_enemy_soldier);
                    }
                }
 
            }
        }

        //battle finish three outcomes
            //draw, routes solider go back
        if (engagement_count >= 200 || (my_soldiers.isEmpty() && enemy_soldiers.isEmpty())){
            routesSoldierGoBack(my_soldiers, my_routes_soldiers);
            routesSoldierGoBack(enemy_soldiers, enemy_routes_soldiers);
            notifyObservers("Draw");
            // lose, routes solider go back
        } else if (my_soldiers.isEmpty()){
            routesSoldierGoBack(my_soldiers, my_routes_soldiers);
            routesSoldierGoBack(enemy_soldiers, enemy_routes_soldiers); 
            notifyObservers("You lose");
            //win, my routes soliders join troops, enemy routes soliders destroyed
        } else if (enemy_soldiers.isEmpty()){
            routesSoldierGoBack(my_soldiers, my_routes_soldiers);
            enemy_soldiers.clear();
            enemy_routes_soldiers.clear();
            enemy_province.getFaction().removeProvince(enemy_province);
            my_province.getFaction().addProvinces(enemy_province);
            enemy_province.setFaction(my_province.getFaction());
            enemy_province.destoryAllBuilding();
            enemy_province.setSoldiers(my_soldiers);
            //enemy_province.destoryAllBuilding();
            my_province.setSoldiers(new ArrayList<Soldier>());
            notifyObservers("You win");
        }

    }


    public void meleeEngagements(Soldier random_my_soldier, Soldier random_enemy_soldier) {
        Engagements( random_my_soldier,  random_enemy_soldier,  false);

    }

    public void rangedEngagements(Soldier random_my_soldier, Soldier random_enemy_soldier) {
        Engagements( random_my_soldier,  random_enemy_soldier,  true);
    }

    public void Engagements(Soldier random_my_soldier, Soldier random_enemy_soldier, boolean flag) {
        Engagements tmp = new Engagements(random_my_soldier, random_enemy_soldier,my_soldiers,enemy_soldiers, flag);
        tmp.setEngagementCount(engagement_count);
        for (Battle_observer o : Battle_observers) {
            tmp.attach(o);
        }
        int e_flag = tmp.run();
        // 0 means draw
        // 1 means enemy breaks
        // 2 means my breaks
        if (e_flag == 0){
            mySoldiersRemove(random_my_soldier);
            enemySoldiersRemove(random_enemy_soldier);
            myRoutesSoldiersAdd(random_my_soldier);
            enemyRoutesSoldiersAdd(random_enemy_soldier);
        } else if (e_flag == 1){
            enemySoldiersRemove(random_enemy_soldier);
            enemyRoutesSoldiersAdd(random_enemy_soldier);
        } else if (e_flag == 2){
            mySoldiersRemove(random_my_soldier);
            myRoutesSoldiersAdd(random_my_soldier);
        } else if (e_flag == 3){
            System.out.println("error");
        }
        
        engagement_count = tmp.getEngagementCount();

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

    public void mySoldiersRemove(Soldier soldier) {
        my_soldiers.remove(soldier);
    }

    public void enemySoldiersRemove(Soldier soldier) {
        enemy_soldiers.remove(soldier);
    }

    public void myRoutesSoldiersAdd(Soldier soldier) {
        addToSoldiers(my_routes_soldiers, soldier); 
    }

    public void enemyRoutesSoldiersAdd(Soldier soldier) {
        addToSoldiers(enemy_routes_soldiers, soldier); 
    }

    public void routesSoldierGoBack(List<Soldier> soldiers, List<Soldier> routes_soldiers) {
        for (Soldier soldier : routes_soldiers) {
            addToSoldiers(soldiers, soldier);
        }
    }

    public void addToSoldiers(List<Soldier> soldiers, Soldier unit){
        int flag = 0;
        if (isUnitEmpty(unit)){
            return;
        }
        for (Soldier soldier : soldiers) {
            if(soldier.getClass() == unit.getClass()){
                flag =1;
                soldier.setNumTroops(soldier.getNumTroops()+unit.getNumTroops());
            }
        }
        if (flag == 0){
            soldiers.add(unit);
        }
    }

    public boolean isUnitEmpty(Soldier soldier) {
        if (soldier.getNumTroops() > 0){
            return false;
        }
        return true;
    }

    private boolean isRanged(Class<? extends Soldier> random_soldier) {
        if (random_soldier == Artillery.class 
        || random_soldier == HorseArcher.class 
        || random_soldier == MissileInfantry.class){
            return true;
        }
        return false;
    }


}