package unsw.enrolment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class MarkObserver implements Observer {

    @Override
    public void update(String coursecode , String term, String zid, String mark_name, double mark) {

        File filename = new File(coursecode + "-" + term + "-" + zid);

        try {
            if(!filename.exists()) { 
                filename.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(filename.getAbsolutePath(),true);
            fileWriter.write(LocalDateTime.now().toString() + "--" + mark_name + "--" +  mark + "\n");
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    

    }


}
