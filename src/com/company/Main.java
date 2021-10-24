package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class ShipPoint extends Point {
    private boolean isShot;

    public ShipPoint(int x, int y) {
        super(x, y);
        this.isShot = false;
    }

    public boolean isShot() {
        return isShot;
    }

    public void setShot(boolean shot) {
        isShot = shot;
    }
}

class Ship {
    private final String name;
    private final int length;
    private ShipPoint[] shipPoints;
    private boolean isAlive;

    public Ship(String name,  ShipPoint[] shipPoints) {
        this.name = name;
        this.isAlive = true;
        this.shipPoints = shipPoints;
        this.length = shipPoints.length;
    }

    public ShipPoint[] getShipPoints() {
        return shipPoints;
    }

    public void setShipPoints(ShipPoint[] shipPoints) {
        this.shipPoints = shipPoints;
    }
}

class Stage {
    private Ship[] ships;

    public Stage() {
        ships = new Ship[5];
    }

    public Ship[] getShips() {
        return ships;
    }

    public void setShips(Ship[] ships) {
        this.ships = ships;
    }
}


class BattleShipController {
    private Stage stage;
    private Scanner scanner;

    public BattleShipController (Stage stage, Scanner scanner) {
        this.stage = stage;
        this.scanner = scanner;
    }

    public void showStage() {
        char characterToDraw = 'A';
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 10; j++) {
                if (pointIsShot(new Point(i,j))) {
                    System.out.print("X");
                }else if (pointIsShip(new Point(i,j))) {
                    System.out.print("O");
                } else if (i == 0 && j == 0) {
                    System.out.print(" ");
                } else if (i == 0) {
                    System.out.print(j);
                } else if (j == 0) {
                    System.out.print(characterToDraw);
                    characterToDraw++;
                } else {
                    System.out.print("~");
                }
                if (j < 10) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }


    public boolean pointIsShip(Point point) {
        for (Ship ship : this.stage.getShips()) {
            if (ship != null) {
                for (ShipPoint shipPoint : ship.getShipPoints()) {
                    if (shipPoint.equals(point)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public boolean pointIsShot(Point point) {
        for (Ship ship : this.stage.getShips()) {
            if (ship != null) {
                for (ShipPoint shipPoint : ship.getShipPoints()) {
                    if (shipPoint.equals(point) && shipPoint.isShot()) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private boolean checkFromLeft(Point point) {
        if (point.x == 10) {
            return false;
        } else return (pointIsShip(new Point(point.x + 1, point.y)));
    }

    private boolean checkFromRight(Point point) {
        if (point.x == 1) {
            return false;
        } else return (pointIsShip(new Point(point.x - 1, point.y)));
    }

    private boolean checkFromUp(Point point) {
        if (point.y == 10) {
            return false;
        } else return (pointIsShip(new Point(point.x, point.y + 1)));
    }

    private boolean checkFromDown(Point point) {
        if (point.y == 1) {
            return false;
        } else return (pointIsShip(new Point(point.x, point.y - 1)));
    }


    private boolean checkAll(Point point) {
        return checkFromLeft(point) || checkFromRight(point) ||
                checkFromUp(point) || checkFromDown(point) ||
                pointIsShip(point);
    }

    public boolean checkShipLength(Point beginningCoordinate, Point endCoordinate, int shipLength) {
        if (beginningCoordinate.distance(endCoordinate) == shipLength - 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkShipLocation(Point beginningCoordinate, Point endCoordinate) {
        if ((beginningCoordinate.x == endCoordinate.x || beginningCoordinate.y == endCoordinate.y)) {
            return true;
        } else {
            return false;
        }
    }

    public Point getCoordinate(Scanner scanner) {
        String scannedCoordinate = scanner.next();
        Point coordinate = new Point();
        coordinate.x = Character.getNumericValue(scannedCoordinate.charAt(0)) - 9;
        coordinate.y = Integer.parseInt(scannedCoordinate.substring(1));
        if (coordinate.x < 1 || coordinate.x > 10 || coordinate.y < 1 || coordinate.y > 10) {
            throw new InputMismatchException("Error, coordinate must have the following format: XN, X = A - J, N = 1 - 10.");
        } else {
            return coordinate;
        }
    }

    public void normalizeCoordinates(Point beginningCoordinate, Point endCoordinate) {
        if (beginningCoordinate.x == endCoordinate.x) {
            if (beginningCoordinate.y > endCoordinate.y) {
                int temp = beginningCoordinate.y;
                beginningCoordinate.y = endCoordinate.y;
                endCoordinate.y = temp;
            }
        }
        if (beginningCoordinate.y == endCoordinate.y) {
            if (beginningCoordinate.x > endCoordinate.x) {
                int temp = beginningCoordinate.x;
                beginningCoordinate.x = endCoordinate.x;
                endCoordinate.x = temp;
            }
        }
    }

    public Ship getShip(Point beginningCoordinate, Point endCoordinate, String name) {
        int length = (int) beginningCoordinate.distance(endCoordinate) + 1;
        ShipPoint[] shipPoints = new ShipPoint[length];
        if (beginningCoordinate.x == endCoordinate.x) {
            for (int i = 0; i < length; i++) {
                shipPoints[i] = new ShipPoint(beginningCoordinate.x, beginningCoordinate.y + i);
            }
        } else {
            for (int i = 0; i < length; i++) {
                shipPoints[i] = new ShipPoint(beginningCoordinate.x + i, beginningCoordinate.y);
            }
        }
        Ship ship = new Ship(name, shipPoints);
        return ship;
    }

    public boolean checkPuttingShip(Ship ship) {
        for (Point point : ship.getShipPoints()) {
            if (checkAll(point)) {
                return false;
            }
        }
        return true;
    }

    private void putShipIntoStageArray(Ship ship) {
        for (int i = 0; i < this.stage.getShips().length; i++) {
            if (this.stage.getShips()[i] == null) {
                this.stage.getShips()[i] = ship;
                return;
            }
        }
    }



    public void putShipIntoStage(String name, int length) {
        System.out.println("Enter the coordinates of the " + name + " (" + length + " cells):");
        while (true) {
            try {
                Point beginningCoordinate = getCoordinate(scanner);
                Point endCoordinate = getCoordinate(scanner);


            if (!checkShipLocation(beginningCoordinate, endCoordinate)) {
                System.out.println("Error! Wrong ship location! Try again:");
                continue;
            }

            normalizeCoordinates(beginningCoordinate, endCoordinate);

            if (!checkShipLength(beginningCoordinate, endCoordinate, length)) {
                System.out.println("Error! Wrong length of the " + name + "! Try again:");
                continue;
            }
            if (!checkPuttingShip(getShip(beginningCoordinate, endCoordinate, name))) {
                System.out.println("Error! You placed it too close to another one. Try again:");
                continue;
            }

            putShipIntoStageArray(getShip(beginningCoordinate, endCoordinate, name));
            this.showStage();
            break;

            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public void populateStage() {
        putShipIntoStage("Aircraft Carrier", 5);
        putShipIntoStage("Battleship", 4);
        putShipIntoStage("Submarine", 3);
        putShipIntoStage("Cruiser", 3);
        putShipIntoStage("Destroyer", 2);
    }
}

public class Main {

    public static void main(String[] args) {
	Stage stage = new Stage();
	Scanner scanner = new Scanner(System.in);
	BattleShipController battleShipController = new BattleShipController(stage, scanner);
    battleShipController.showStage();
	battleShipController.populateStage();

    }
}
