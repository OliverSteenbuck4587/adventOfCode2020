import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Trees31 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(new File("/home/osteenbu/otto/adventOfCode/src/main/resources/3trees.txt"));
        ArrayList<Road> roads = new ArrayList<>();
        while (s.hasNext()) {
            roads.add(new Road(s.next().trim()));
        }
        s.close();
        int treesHit = 0;
        int position = 0;
        for(Road road: roads){

            if(road.getGroundType(position) == GroundType.TREE){
                treesHit++;
            }
            position = nextTouchpoint(position);
        }
        System.out.println(treesHit);

    }

    public static int nextTouchpoint(int lastTouchpoint){
        return lastTouchpoint + 3;
    }

    public enum GroundType {
        TREE,
        FREE;
    }

    private static class Road {
        String roadPart;

        public Road(String roadPartString) {
            roadPart = roadPartString;
        }

        public GroundType getGroundType(int position){
            while(position > roadPart.length() - 1){
                roadPart = roadPart + roadPart;
            }
            if(roadPart.charAt(position) == '#')
                return GroundType.TREE;
            else
                return GroundType.FREE;

        }

    }
}
