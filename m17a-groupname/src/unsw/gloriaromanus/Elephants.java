package unsw.gloriaromanus;

import java.io.IOException;

public class Elephants extends Soldier {

    public Elephants(Province province) throws IOException {
        super(province, "src/unsw/gloriaromanus/Elephant.json");
    }
}