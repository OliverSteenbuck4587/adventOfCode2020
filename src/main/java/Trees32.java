import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class Trees32 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(new File("/home/osteenbu/otto/adventOfCode/src/main/resources/3trees.txt"));
        Road road = new Road();
        while (s.hasNext()) {
            final RoadPart roadPart = new RoadPart(s.next().trim());
            road.addRoadPart(roadPart);
        }
        s.close();
        List<StepFunction> stepFunctions = new ArrayList<>();
        stepFunctions.add(new StepFunction((Point p) -> new Point(p.x + 1, p.y + 1)));
        stepFunctions.add(new StepFunction((Point p) -> new Point(p.x + 1, p.y + 3)));
        stepFunctions.add(new StepFunction((Point p) -> new Point(p.x + 1, p.y + 5)));
        stepFunctions.add(new StepFunction((Point p) -> new Point(p.x + 1, p.y + 7)));
        stepFunctions.add(new StepFunction((Point p) -> new Point(p.x + 2, p.y + 1)));
        int result = 1;
        for(StepFunction stepFunction: stepFunctions) {
            int treesHit = 0;
            Point currentPosition = new Point(0, 0);
            GroundType currentTile;

            do {
                currentPosition = stepFunction.stepFunction.apply(currentPosition);

                currentTile = road.getGroundType(currentPosition);
                if (currentTile == GroundType.TREE) {
                    treesHit++;
                }
            } while (currentTile != GroundType.FINISH);
            result = result * treesHit;
            System.out.println(treesHit);
        }
        System.out.println("Result: " + result);

    }


    public enum GroundType {
        TREE,
        FREE,
        FINISH;

    }

    private static class StepFunction {
        final Function<Point, Point> stepFunction;

        private StepFunction(Function<Point, Point> stepFunction) {
            this.stepFunction = stepFunction;
        }
    }

    private static class Road {
        List<RoadPart> roadParts = new ArrayList<>();

        void addRoadPart(RoadPart roadPart) {
            roadParts.add(roadPart);
        }

        GroundType getGroundType(Point point) {
            if (point.x >= roadParts.size())
                return GroundType.FINISH;
            return roadParts.get(point.x).getGroundType(point.y);
        }

    }


    private static class RoadPart {
        String roadPart;

        public RoadPart(String roadPartString) {
            roadPart = roadPartString;
        }

        public GroundType getGroundType(int position) {
            while (position > roadPart.length() - 1) {
                roadPart = roadPart + roadPart;
            }
            if (roadPart.charAt(position) == '#')
                return GroundType.TREE;
            else
                return GroundType.FREE;

        }

    }
}
