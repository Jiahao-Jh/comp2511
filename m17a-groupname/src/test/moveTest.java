package test;

import unsw.gloriaromanus.Faction;
import unsw.gloriaromanus.Province;

public class moveTest {


    public static void main(String[] args) {
        Faction f = new Faction("test");
        Province Britannia = new Province("Britannia", f);
        Province Lugdunensis = new Province("Lugdunensis", f);
        Province Aquitania = new Province("Aquitania", f);
        f.addProvinces(Britannia);
        f.addProvinces(Lugdunensis);
        f.addProvinces(Aquitania);
        f.constructGraph();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(f.getGraph().getBean(i, j).toString());
            }
        }
        
    }
}
