import java.util.*;
import java.io.*;
import java.math.*;
import java.awt.Point;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    private static int lowXBoundary, highXBoundary, lowYBoundary, highYBoundary;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt(); // width of the building.
        int height = in.nextInt(); // height of the building.
        int numberOfTurns = in.nextInt(); // maximum number of turns before game over.
        int startX = in.nextInt();
        int startY = in.nextInt();

        Point currentPoint = new Point(startX, startY);

        lowXBoundary = 0;
        highXBoundary = width - 1;
        lowYBoundary = 0;
        highYBoundary = height - 1;

        // game loop
        while (true) {
            String bombDir = in.next(); // the direction of the bombs from batman's current location (U, UR, R, DR, D, DL, L or UL)

            Direction bombDirection = getBombDirection(bombDir);

            if (bombDirection == Direction.NONE) {
                System.err.println("Error: No direction found");
            }

            updateBoundaries(currentPoint, bombDirection);
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            
            Point delta = getDelta(bombDirection);

            if (delta == new Point(0, 0)) {
                System.err.println("Error: No direction delta found");
            }

            System.err.println("delta: " + delta);

            System.err.println("lowYBoundary: " + lowYBoundary);
            System.err.println("highYBoundary: " + highYBoundary);

            int possibleMoves = calculatePossibleMoves(currentPoint, delta);

            System.err.println("possibleMoves: " + possibleMoves);

            Point move = new Point(0,0);
            if (possibleMoves == 1) {
                 move = calculateMove(currentPoint, delta, possibleMoves);
            } else {
                move = calculateMove(currentPoint, delta, possibleMoves/2);
            }

            currentPoint = new Point(move.x, move.y);
            
            System.err.println("move: " + move);

            // the location of the next window Batman should jump to.
            System.out.println(move.x + " " + move.y);
        }
    }

    private static Direction getBombDirection(String bombDir) {
        switch(bombDir) {
            case "U":
                return Direction.UP;
            case "UR":
                return Direction.UP_RIGHT;
            case "R":
                return Direction.RIGHT;
            case "DR":
                return Direction.DOWN_RIGHT;
            case "D":
                return Direction.DOWN;
            case "DL":
                return Direction.DOWN_LEFT;
            case "L":
                return Direction.LEFT;
            case "UL":
                return Direction.UP_LEFT;
        }

        return Direction.NONE;
    }

    private static Point getDelta(Direction bombDirection) {
        switch(bombDirection) {
            case  Direction.UP:
                return new Point(0, -1);
            case Direction.UP_RIGHT:
                return new Point(1, -1);
            case Direction.RIGHT:
                return new Point(1, 0);
            case Direction.DOWN_RIGHT:
                return new Point(1, 1);
            case Direction.DOWN:
                return new Point(0, 1);
            case Direction.DOWN_LEFT:
                return new Point(-1, 1);
            case Direction.LEFT:
                return new Point(-1, 0);
            case Direction.UP_LEFT:
                return new Point(-1, -1);
            case Direction.NONE:
                return new Point(0, 0);
        }

        return new Point(0,0);
    }

    private static int calculatePossibleMoves(Point currentPoint, Point delta) {
        int moves = 0;

        Point checkPoint = new Point(currentPoint.x + delta.x, currentPoint.y + delta.y);

        while (isWithinBounds(checkPoint)) {
            moves++;
            checkPoint = new Point(checkPoint.x + delta.x, checkPoint.y + delta.y);         
        }

        return moves;
    }

    private static Point calculateMove(Point currentPoint, Point delta, int numberOfMoves) {
        return new Point(currentPoint.x + (delta.x * numberOfMoves), currentPoint.y + (delta.y * numberOfMoves));
    }


    private static Boolean isWithinBounds(Point point) {
        if (point.x < lowXBoundary || point.x > highXBoundary || point.y < lowYBoundary || point.y > highYBoundary) {
            return false;
        } 

        return true;
    }

    private static void updateBoundaries(Point point, Direction bombDirection) {
        
        switch(bombDirection) {
            case  Direction.UP:
                highYBoundary = point.y + 1;
                break;
            case Direction.UP_RIGHT:
                highYBoundary = point.y + 1;
                lowXBoundary = point.x - 1;
                break;
            case Direction.RIGHT:
                lowXBoundary = point.x - 1;
                break;
            case Direction.DOWN_RIGHT:
                lowYBoundary = point.y - 1;
                lowXBoundary = point.x - 1;
                break;
            case Direction.DOWN:
                lowYBoundary = point.y - 1;
                break;
            case Direction.DOWN_LEFT:
                lowYBoundary = point.y - 1;
                highXBoundary = point.x + 1;
                break;
            case Direction.LEFT:
                highXBoundary = point.x + 1;
                break;
            case Direction.UP_LEFT:
                highYBoundary = point.y + 1;
                highXBoundary = point.x + 1;
                break;
            case Direction.NONE:
                break;
        }
    }
}

enum Direction {
    NONE,
    UP, 
    UP_RIGHT, 
    RIGHT, 
    DOWN_RIGHT, 
    DOWN, 
    DOWN_LEFT, 
    LEFT,
    UP_LEFT
}