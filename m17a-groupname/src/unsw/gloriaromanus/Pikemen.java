package unsw.gloriaromanus;

import java.io.IOException;

public class Pikemen extends Soldier {

    public Pikemen(Province province) throws IOException {
        super(province, "src/unsw/gloriaromanus/Pikemen.json");
    }
}