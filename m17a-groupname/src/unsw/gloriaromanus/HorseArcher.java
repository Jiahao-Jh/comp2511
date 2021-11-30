package unsw.gloriaromanus;

import java.io.IOException;

public class HorseArcher extends Soldier {

    public HorseArcher(Province province) throws IOException {
        super(province, "src/unsw/gloriaromanus/HorseArchor.json");
    }
}