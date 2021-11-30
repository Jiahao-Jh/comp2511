package unsw.gloriaromanus;

import java.io.IOException;

public class HeavyInfantry extends Soldier {

    public HeavyInfantry(Province province) throws IOException {
        super(province, "src/unsw/gloriaromanus/HeavyInfantry.json");
    }
}