package unsw.gloriaromanus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class saveAndLoad {

    
    public void saveGame(String filename, GloriaRomanus g) throws IOException, ClassNotFoundException {
        filename = "src/savedGame/" + filename;

        FileOutputStream f = new FileOutputStream(new File(filename));
        ObjectOutputStream o = new ObjectOutputStream(f);
        // Write objects to file
        o.writeObject(g);
        o.close();
        f.close();
    }

    public GloriaRomanus loadGame(String filename) throws IOException, ClassNotFoundException {
        filename = "src/savedGame/" + filename;



            FileInputStream fi = new FileInputStream(new File(filename));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            return (GloriaRomanus) oi.readObject();


    }
}
