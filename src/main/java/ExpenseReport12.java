import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class ExpenseReport12 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(new File("/home/osteenbu/otto/adventOfCode/src/main/resources/expenseReport.txt"));
        ArrayList<Integer> lines = new ArrayList<Integer>();
        while (s.hasNext()){
            lines.add(Integer.valueOf(s.next()));
        }
        s.close();
        for (Integer lineItem: lines) {
            for (Integer lineItem2: lines) {
                for (Integer lineItem3: lines) {
                    if (lineItem + lineItem2 + lineItem3 == 2020) {
                        System.out.println(lineItem * lineItem2 * lineItem3);
                        System.out.println(lineItem +" "+ lineItem2 +" "+ lineItem3);
                        System.exit(0);
                    }
                }

            }
        }
    }

}
