package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import org.junit.Rule;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import unsw.gloriaromanus.*;

public class UnitTest {

    @Test
    public void MoveTest() throws IOException {
        Faction f = new Faction("test");
        Province Lugdunensis = new Province("Lugdunensis", f);
        Province Aquitania = new Province("Aquitania", f);
        f.addProvinces(Lugdunensis);
        f.addProvinces(Aquitania);
        f.constructGraph();
        assertTrue(f.getGraph().getBean(1, 0));
        int[] pred = new int[2];
        assertTrue(f.getGraph().BFS(0, 1, pred));

        Chariot a = new Chariot(Lugdunensis);
        a.move(Aquitania);
        assertEquals(a.getProvince(), Aquitania);
    }

    @Test
    // movement point reset
    public void MoveTest1() throws IOException {
        Faction f = new Faction("test");
        Province Britannia = new Province("Britannia", f);
        Province Lugdunensis = new Province("Lugdunensis", f);
        Province Aquitania = new Province("Aquitania", f);
        f.addProvinces(Britannia);
        f.addProvinces(Lugdunensis);
        f.addProvinces(Aquitania);
        f.constructGraph();

        Chariot a = new Chariot(Britannia);
        //should not work
        a.setRemainMp(4);
        a.move(Aquitania);
        assertEquals(a.getProvince(), Britannia);
        // //should work
        a.move(Lugdunensis);
        assertEquals(a.getProvince(), Lugdunensis);
        // //should not work
        a.move(Aquitania);
        assertEquals(a.getProvince(), Lugdunensis);
        // //should work
        a.resetMovementPoints();
        a.move(Aquitania);
        assertEquals(a.getProvince(), Aquitania);
    }

    @Test
    public void blahTest() {
        assertEquals("a", "a");
    }

    @Test
    public void blahTest2() {
        Unit u = new Unit();
        assertEquals(u.getNumTroops(), 50);
    }

    @Test
    public void StateTest() {
        Faction f = new Faction("test");
        Province p = new Province("test", f);
        assertEquals(p.getState().toString(), "vacant");
    }


    @Test
    public void BattleTest1() throws IOException {
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
        //should print Your win

        
    }

    @Test
    public void BattleTest2() throws IOException {
        Faction f = new Faction("test");
        Faction f1 = new Faction("test2");

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
        //can't predict print 
    }
    
    @Test
    public void BattleTest3() throws IOException {
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
        //can't predict print 

    }

    @Test
    public void BattleTest4() throws IOException {
        Faction f = new Faction("test");
        Faction f1 = new Faction("test12");

        Province p = new Province("test", f);
        Chariot c = new Chariot(p);
        MissileInfantry mi = new MissileInfantry(p);
        HorseArcher h = new HorseArcher(p);
        mi.setNumTroops(1000);
        c.setNumTroops(1000);
        List<Soldier> soldiers = new ArrayList<>();
        soldiers.add(c);
        soldiers.add(mi);
        soldiers.add(h);
        p.setSoldiers(soldiers);

        Province p1 = new Province("test1", f1);
        MeleeCavalry m = new MeleeCavalry(p1);
        MissileInfantry mi2 = new MissileInfantry(p1);
        HorseArcher h1 = new HorseArcher(p1);
        mi2.setNumTroops(1000);
        m.setNumTroops(1000);
        Artillery a = new Artillery(p1);
        List<Soldier> soldiers1 = new ArrayList<>();
        soldiers1.add(m);
        soldiers1.add(a);
        soldiers1.add(mi2);
        soldiers1.add(h1);
        p1.setSoldiers(soldiers1);

        Battle_observer_for_test o = new Battle_observer_for_test();
        Battle_resolver b = new Battle_resolver(p,p1);
        b.attach(o);
        b.detach(o);
        b.attach(o);
        b.startSkirmish(); 

    
    }

    @Test
    public void endturnTest1() throws IOException {
        GloriaRomanus g = new GloriaRomanus(2);
        Faction f = g.getFaction("Rome");
        Province p = new Province("Britannia", f);
        f.addProvinces(p);
        List<Soldier> soldiers1 = new ArrayList<>();
        Artillery a = new Artillery(p);
        soldiers1.add(a);
        p.setSoldiers(soldiers1);
        g.performAction("end");

        //should print Your win this game
    }

    @Test
    public void saveTest() throws IOException, ClassNotFoundException {
        saveAndLoad s = new saveAndLoad();
        GloriaRomanus g = new GloriaRomanus(2);
        g.setGoal("TREASURY");
        s.saveGame("savegame1.txt",g);
 

    }

    @Test
    public void loadTest() throws IOException, ClassNotFoundException {
        saveAndLoad s = new saveAndLoad();
        GloriaRomanus g = s.loadGame("savegame1.txt");
        assertEquals(1, g.getTreasuryGoal()); 

    }


    @Test
    public void victoryTest1() throws IOException {
        GloriaRomanus g = new GloriaRomanus(2);
        g.setGoal("TREASURY");
        Faction f1 = g.getFaction("qqqq");
        assertNull(f1);
        Faction f = g.getFaction("Rome");
        g.setCurrFaction(f);
        f.setTreasury(500000);
        g.checkVictory();
        //should print Your win this game
    }

    @Test
    public void victoryTest2() throws IOException {
        GloriaRomanus g = new GloriaRomanus(2);
        g.setGoal("WEALTH");
        Faction f = g.getFaction("Rome");
        g.setCurrFaction(f);
        f.setWealth(500000);
        g.checkVictory();
        //should print Your win this game
    }

    @Test
    public void victoryTest3() throws IOException {
        GloriaRomanus g = new GloriaRomanus(2);
        g.setGoal("CONQUEST");
        Faction f = g.getFaction("Rome");
        g.setCurrFaction(f);
        for (Faction i : g.getFactions()) {
            if (!i.getName().equals("Rome")){
                for (Province p : i.getProvinces()) {
                    p.setFaction(f);
                }
            }
        }
        g.checkVictory();
        //should print Your win this game
    }


    @Test 
    public void TaxTest() throws IOException {
        Faction f = new Faction("test");
        Province p = new Province("a", f);
        f.addProvinces(p);
        f.increaseTreasury();
        assertEquals(200, p.getWealth());
        assertEquals(330, f.getTreasury());

        p.changeTaxRate("low");
        assertEquals(p.getLowTax(),p.getCurrRate());
        f.increaseTreasury();
        assertEquals(210, p.getWealth());
        assertEquals(351, f.getTreasury());

        // does nothing 
        p.changeTaxRate("low");
        assertEquals(p.getLowTax(),p.getCurrRate());

        Chariot c = new Chariot(p);
        p.addSoldier(c);
        p.changeTaxRate("very high");
        assertEquals(p.getVeryHighTax(),p.getCurrRate());
        f.increaseTreasury();
        //minus 1 morale
        assertEquals(c.getMorale(), 9);
        assertEquals(180, p.getWealth());

        p.changeTaxRate("high");
        assertEquals(p.getHighTax(),p.getCurrRate());
        f.increaseTreasury();
        assertEquals(170, p.getWealth());
    }

    @Test
    public void ProductionTest() throws IOException {
        Faction f = new Faction("test");
        Province p = new Province("p", f);
        f.addProvinces(p);
        TroopProductionBuilding t = new TroopProductionBuilding(p);
        p.start(t);
        assertEquals(p.getConstructing(), t);
        p.work();
        assertEquals(t, p.getTroopProductionBuilding());
        assertEquals(200, f.getTreasury());


        Chariot c1 = new Chariot(p);
        t.start(c1);
        assertEquals(t.getSpots().get(0).getOccupied(), t.getSpots().get(0).getState());
        assertEquals(c1, t.getSpots().get(0).getTrainning());
        assertEquals(80, f.getTreasury());

        // train 1 turn
        t.work();
        assertEquals(3, t.getSpots().get(0).getRemainTurns());
        //work 4 turns

        // c2 should fail not enough money
        Chariot c2 = new Chariot(p);
        t.start(c2);
        assertEquals(t.getSpots().get(1).getVacant(), t.getSpots().get(1).getState());
        assertEquals(null, t.getSpots().get(1).getTrainning());
        t.work();
        t.work();
        t.work();
        assertTrue(p.getSoldiers().contains(c1));

    }

    @Test
    public void TrainBothTest() throws IOException {
        Faction f = new Faction("test");
        Province p = new Province("a", f);
        f.addProvinces(p);
        // obtain enough money
        f.increaseTreasury();
        f.increaseTreasury();
        f.increaseTreasury();
        f.increaseTreasury();
        TroopProductionBuilding t = new TroopProductionBuilding(p);
        p.start(t);
        assertEquals(p.getConstructing(), t);
        p.work();
        Chariot c1 = new Chariot(p);
        t.start(c1);
        assertEquals(t.getSpots().get(0).getOccupied(), t.getSpots().get(0).getState());
        assertEquals(c1, t.getSpots().get(0).getTrainning());
        Chariot c2 = new Chariot(p);
        t.start(c2);
        assertEquals(t.getSpots().get(1).getOccupied(), t.getSpots().get(1).getState());
        assertEquals(c2, t.getSpots().get(1).getTrainning());
    }

    @Test 
    public void MineTest() throws IOException {
        Faction f = new Faction("test");
        Province p = new Province("a", f);
        // -80
        Mine m = new Mine(p);
        f.addProvinces(p);
        p.start(m);
        p.work();
        assertEquals(m, p.getMine());
        // -100
        TroopProductionBuilding t = new TroopProductionBuilding(p);
        p.start(t);
        p.work();
        
        // -114
        Chariot c = new Chariot(p);
        t.start(c);
        assertEquals(6, f.getTreasury());

        m.upgrade();
        assertEquals(1, m.getLevel());
        // get some money
        for (int i = 0; i < 10; i++) {
            f.increaseTreasury();
        }
        assertTrue(m.upgradable());
        m.upgrade();
        assertEquals(2, m.getLevel());

        Chariot c2 = new Chariot(p);
        t.start(c2);
        assertEquals(168, f.getTreasury());
        m.upgrade();
        m.upgrade();
        assertEquals(3, m.getLevel());
    }

    @Test
    public void FarmTest() throws IOException {
        Faction f = new Faction("test");
        Province p = new Province("a", f);
        f.addProvinces(p);
        TroopProductionBuilding t = new TroopProductionBuilding(p);
        p.start(t);
        p.work();

        Farm m = new Farm(p);
        p.start(m);
        p.work();

        assertEquals(m, p.getFarm());

        Chariot c1 = new Chariot(p);
        t.start(c1);
        assertEquals(3, t.getSpots().get(0).getRemainTurns());
    }

    @Test
    public void RepairTest1() {
        Faction f = new Faction("test");
        Province p = new Province("a", f);
        f.addProvinces(p);
        p.destroyed();
        assertEquals(p.getDestroyed(), p.getState());
        p.repair();
        assertEquals(p.getRepair(), p.getState());
        assertEquals(1, p.getRemainTurns());

        p.work();
        assertEquals(p.getVacant(), p.getState());
    }

    @Test
    public void RepairTest2() throws IOException {
        Faction f = new Faction("test");
        Province p = new Province("a", f);
        f.addProvinces(p);
        TroopProductionBuilding t = new TroopProductionBuilding(p);
        p.start(t);
        p.work();

        Chariot c = new Chariot(p);
        t.start(c);
        t.destroyed();
        assertEquals(80, f.getTreasury());
        for (ProductionSpot s : t.getSpots()) {
            assertEquals(s.getDestroyed(), s.getState());
        }
        t.repair();
        for (ProductionSpot s : t.getSpots()) {
            assertEquals(s.getRepair(), s.getState());
        }
        assertEquals(1, t.getSpots().get(0).getRemainTurns());
        p.work();
        assertEquals(0, t.getSpots().get(0).getRemainTurns());
        for (ProductionSpot s : t.getSpots()) {
            assertEquals(s.getVacant(), s.getState());
        }
    }
}

