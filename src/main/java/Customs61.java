import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Customs61 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(new File("/home/osteenbu/otto/adventOfCode/src/main/resources/5BoardingPass.txt"));
        ArrayList<String> seatNumbers = new ArrayList<>();
        while (s.hasNext()) {
            seatNumbers.add(s.next().trim());
        }
        s.close();

        final List<Integer> takenSeats = seatNumbers.stream().map(BoardingPass51::toSeatId).sorted().collect(Collectors.toList());
        for (int i = 1; i < takenSeats.size(); i++) {
            int currentId = takenSeats.get(i);
            int idBefore = takenSeats.get(i-1);
            if(currentId-idBefore != 1){
                System.out.println(currentId - 1);
            }
        }
    }

}
