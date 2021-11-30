package unsw.gloriaromanus;

import java.io.IOException;

public class Artillery extends Soldier {

    public Artillery(Province province) throws IOException {
        super(province, "src/unsw/gloriaromanus/Artillery.json");
    }
}