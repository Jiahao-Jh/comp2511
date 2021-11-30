package example;
import java.util.*;

public class Splitter {

    public static void main(String[] args){
        System.out.println("Enter a sentence specified by spaces only: ");
        // Add your code
        Scanner in = new Scanner(System.in);
        String tmp = in.nextLine();
        String[] arr = tmp.split(" ");    

        for ( String a : arr) {
            System.out.println(a);
        }
        in.close();

        
    }
}
