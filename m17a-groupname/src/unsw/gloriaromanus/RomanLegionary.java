package unsw.gloriaromanus;

import java.io.IOException;

public class RomanLegionary extends Soldier {

    public RomanLegionary(Province province) throws IOException {
        super(province, "src/unsw/gloriaromanus/RomanLegionary.json");
    }
}