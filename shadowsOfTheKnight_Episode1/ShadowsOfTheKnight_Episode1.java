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

            int xPos = 0;
            int yPos = 0;

            switch(bombDirection) {
                case  Direction.UP:
                    xPos = currentPoint.x;
                    yPos = getUpMove(currentPoint);
                    break;
                case Direction.UP_RIGHT:
                    xPos = getRightMove(currentPoint);
                    yPos = getUpMove(currentPoint);
                    break;
                case Direction.RIGHT:
                    xPos = getRightMove(currentPoint);
                    yPos = currentPoint.y;
                    break;
                case Direction.DOWN_RIGHT:
                    xPos = getRightMove(currentPoint);
                    yPos = getDownMove(currentPoint);
                    break;
                case Direction.DOWN:
                    xPos = currentPoint.x;
                    yPos = getDownMove(currentPoint);
                    break;
                case Direction.DOWN_LEFT:
                    xPos = getLeftMove(currentPoint);
                    yPos = getDownMove(currentPoint);
                    break;
                case Direction.LEFT:
                    xPos = getLeftMove(currentPoint);
                    yPos = currentPoint.y;
                    break;
                case Direction.UP_LEFT:
                    xPos = getLeftMove(currentPoint);
                    yPos = getUpMove(currentPoint);
                    break;
                case Direction.NONE:
                    break;
            }

            currentPoint = new Point(xPos, yPos);

            // the location of the next window Batman should jump to.
            System.out.println(currentPoint.x + " " + currentPoint.y);
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

    private static int getUpMove(Point currentPoint) {
    
        if (currentPoint.y - lowYBoundary == 0) {
            return currentPoint.y;
        } else if (currentPoint.y - lowYBoundary == 1) {
            return currentPoint.y - 1;
        } else {
            return currentPoint.y - (int)Math.round((double)(currentPoint.y - lowYBoundary) / 2);
        }
    }

    private static int getLeftMove(Point currentPoint) {
    
        if (currentPoint.x - lowXBoundary == 0) {
            return currentPoint.x;
        } else if (currentPoint.x - lowXBoundary == 1) {
            return currentPoint.x - 1;
        } else {
            return currentPoint.x - (int)Math.round((double)(currentPoint.x - lowXBoundary) / 2);
        }
    }

    private static int getRightMove(Point currentPoint) {
        
        if (highXBoundary - currentPoint.x == 0) {
            return currentPoint.x;
        } else if (highXBoundary - currentPoint.x == 1) {
            return currentPoint.x + 1;
        } else {
            return currentPoint.x + (int)Math.round((double)(highXBoundary - currentPoint.x) / 2.0);
        }
    }

    private static int getDownMove(Point currentPoint) {
    
        if (highYBoundary - currentPoint.y == 0) {
            return currentPoint.y;
        } else if (highYBoundary - currentPoint.y == 1) {
            return currentPoint.y + 1;
        } else {
            return currentPoint.y + (int)Math.round((double)(highYBoundary - currentPoint.y) / 2);
        }
    }

    private static void updateBoundaries(Point point, Direction bombDirection) {
        
        switch(bombDirection) {
            case  Direction.UP:
                highYBoundary = point.y - 1;
                break;
            case Direction.UP_RIGHT:
                highYBoundary = point.y - 1;
                lowXBoundary = point.x + 1;
                break;
            case Direction.RIGHT:
                lowXBoundary = point.x + 1;
                break;
            case Direction.DOWN_RIGHT:
                lowYBoundary = point.y + 1;
                lowXBoundary = point.x + 1;
                break;
            case Direction.DOWN:
                lowYBoundary = point.y + 1;
                break;
            case Direction.DOWN_LEFT:
                lowYBoundary = point.y + 1;
                highXBoundary = point.x - 1;
                break;
            case Direction.LEFT:
                highXBoundary = point.x - 1;
                break;
            case Direction.UP_LEFT:
                highYBoundary = point.y - 1;
                highXBoundary = point.x - 1;
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