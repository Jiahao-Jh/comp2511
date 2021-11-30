package unsw.gloriaromanus;

import java.io.IOException;

public class MissileInfantry extends Soldier {

    public MissileInfantry(Province province) throws IOException {
        super(province, "src/unsw/gloriaromanus/MissileInfantry.json");
    }
}