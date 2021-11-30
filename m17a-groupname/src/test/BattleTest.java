package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import unsw.gloriaromanus.*;

public class BattleTest {
    public static void main(String[] args) throws IOException {
        BattleTest b = new BattleTest();
        // print -- You win
        //b.test1();
        //
        //b.test2();
        //
        b.test3();

    }


    public void test1() throws IOException {
        Faction f = new Faction("test");
        Faction f1 = new Faction("test1");

        Province p = new Province("test", f);
        Chariot c = new Chariot(p);
        List<Soldier> soldiers = new ArrayList<>();
        soldiers.add(c);
        p.setSoldiers(soldiers);

        Province p1 = new Province("test1", f1);

        Battle_observer_for_test o = new Battle_observer_for_test();
        Battle_resolver b = new Battle_resolver(p,p1);
        b.attach(o);
        b.startSkirmish(); 
    }

    public void test2() throws IOException {
        Faction f = new Faction("test");
        Faction f1 = new Faction("test1");

        Province p = new Province("test", f);
        Chariot c = new Chariot(p);
        List<Soldier> soldiers = new ArrayList<>();
        soldiers.add(c);
        p.setSoldiers(soldiers);

        Province p1 = new Province("test1", f1);
        MeleeCavalry m = new MeleeCavalry(p1);
        List<Soldier> soldiers1 = new ArrayList<>();
        soldiers1.add(m);
        p1.setSoldiers(soldiers1);

        Battle_observer_for_test o = new Battle_observer_for_test();
        Battle_resolver b = new Battle_resolver(p,p1);
        b.attach(o);
        b.startSkirmish(); 
    }

    public void test3() throws IOException {
        Faction f = new Faction("test");
        Faction f1 = new Faction("test1");

        Province p = new Province("test", f);
        Chariot c = new Chariot(p);

        List<Soldier> soldiers = new ArrayList<>();
        soldiers.add(c);
        p.setSoldiers(soldiers);

        Province p1 = new Province("test1", f1);
        MeleeCavalry m = new MeleeCavalry(p1);
        Artillery a = new Artillery(p1);
        List<Soldier> soldiers1 = new ArrayList<>();
        soldiers1.add(m);
        soldiers1.add(a);
        p1.setSoldiers(soldiers1);

        Battle_observer_for_test o = new Battle_observer_for_test();
        Battle_resolver b = new Battle_resolver(p,p1);
        b.attach(o);
        b.startSkirmish(); 
    }



}
