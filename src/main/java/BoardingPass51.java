import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BoardingPass51 {

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

    public static int toSeatId(String seatNumber) {
        int row = getRow(seatNumber.substring(0, 7));
        int seat = getSeat(seatNumber.substring(7));
        int seatId = (row * 8) + seat;
        return seatId;
    }

    public static int getSeat(String seat) {
        List<Integer> seatTree = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            seatTree.add(i);
        }
        for (char significantRowBit : seat.toCharArray()) {
            if (significantRowBit == 'L') {
                seatTree = seatTree.subList(0, seatTree.size() / 2);
            } else {
                seatTree = seatTree.subList(seatTree.size() / 2, seatTree.size());
            }
        }
        if (seatTree.size() != 1)
            throw new RuntimeException("Error in seat calculation");
        return seatTree.get(0);
    }

    public static int getRow(String row) {
        List<Integer> rowTree = new ArrayList<>();
        for (int i = 0; i < 128; i++) {
            rowTree.add(i);
        }
        for (char significantRowBit : row.toCharArray()) {
            if (significantRowBit == 'F') {
                rowTree = rowTree.subList(0, rowTree.size() / 2);
            } else {
                rowTree = rowTree.subList(rowTree.size() / 2, rowTree.size());
            }
        }
        if (rowTree.size() != 1)
            throw new RuntimeException("Error in row calculation");
        return rowTree.get(0);
    }


}
