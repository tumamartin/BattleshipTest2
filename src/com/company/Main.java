package com.company;

import java.awt.*;
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
        this.isShot = shot;
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

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getLength() {
        return length;
    }
}

class Stage {
    private Ship[] ships;

    private boolean[][] hits;

    public Stage() {
        ships = new Ship[5];
        this.hits = new boolean[11][11];
    }

    public Ship[] getShips() {
        return ships;
    }

    public void setShips(Ship[] ships) {
        this.ships = ships;
    }

    public boolean[][] getHits() {
        return hits;
    }

    public void setHit(Point point) {
        this.hits[point.x][point.y] = true;
    }
}


class BattleShipController {
    private Scanner scanner;
    private boolean gameOn;

    public BattleShipController (Scanner scanner) {
        this.scanner = scanner;
        gameOn = true;
    }

    public void showStage(Stage stage, boolean fog) {
        char characterToDraw = 'A';
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 10; j++) {
                if (pointIsShot(stage, new Point(i,j))) {
                    System.out.print("X");
                }else if (pointIsShip(stage, new Point(i,j)) && !fog) {
                    System.out.print("O");
                } else if (i == 0 && j == 0) {
                    System.out.print(" ");
                } else if (i == 0) {
                    System.out.print(j);
                } else if (j == 0) {
                    System.out.print(characterToDraw);
                    characterToDraw++;
                } else {
                    System.out.print(!stage.getHits()[i][j] ? "~" : "M");
                }
                if (j < 10) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }


    public boolean pointIsShip(Stage stage, Point point) {
        for (Ship ship : stage.getShips()) {
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

    public boolean pointIsShot(Stage stage, Point point) {
        for (Ship ship : stage.getShips()) {
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

    private boolean checkFromLeft(Stage stage, Point point) {
        if (point.x == 10) {
            return false;
        } else return (pointIsShip(stage, new Point(point.x + 1, point.y)));
    }

    private boolean checkFromRight(Stage stage, Point point) {
        if (point.x == 1) {
            return false;
        } else return (pointIsShip(stage, new Point(point.x - 1, point.y)));
    }

    private boolean checkFromUp(Stage stage, Point point) {
        if (point.y == 10) {
            return false;
        } else return (pointIsShip(stage, new Point(point.x, point.y + 1)));
    }

    private boolean checkFromDown(Stage stage, Point point) {
        if (point.y == 1) {
            return false;
        } else return (pointIsShip(stage, new Point(point.x, point.y - 1)));
    }


    private boolean checkAll(Stage stage, Point point) {
        return checkFromLeft(stage, point) || checkFromRight(stage, point) ||
                checkFromUp(stage, point) || checkFromDown(stage, point) ||
                pointIsShip(stage, point);
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
            throw new InputMismatchException();
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

    public boolean checkPuttingShip(Stage stage, Ship ship) {
        for (Point point : ship.getShipPoints()) {
            if (checkAll(stage, point)) {
                return false;
            }
        }
        return true;
    }

    private void putShipIntoStageArray(Stage stage, Ship ship) {
        for (int i = 0; i < stage.getShips().length; i++) {
            if (stage.getShips()[i] == null) {
                stage.getShips()[i] = ship;
                return;
            }
        }
    }



    public void putShipIntoStage(Stage stage, int playerNumber, String name, int length) {
        System.out.println("Player " + playerNumber + ", place your ships on the game field");
        showStage(stage, false);
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
            if (!checkPuttingShip(stage, getShip(beginningCoordinate, endCoordinate, name))) {
                System.out.println("Error! You placed it too close to another one. Try again:");
                continue;
            }

            putShipIntoStageArray(stage, getShip(beginningCoordinate, endCoordinate, name));
            showStage(stage, false);
            break;

            } catch(NumberFormatException|InputMismatchException e) {
                System.out.println("Error, coordinates must have the following format: XN XN, X = A - J, N = 1 - 10.");
                scanner.nextLine();
            }
        }

    }

    public void populateStage(Stage stage, int playerNumber) {
        putShipIntoStage(stage, playerNumber,"Aircraft Carrier", 5);
        putShipIntoStage(stage, playerNumber,"Battleship", 4);
        putShipIntoStage(stage, playerNumber,"Submarine", 3);
        putShipIntoStage(stage, playerNumber,"Cruiser", 3);
        putShipIntoStage(stage, playerNumber,"Destroyer", 2);
    }

    public void shotShip(Stage stage, Point point) {
        for (Ship ship : stage.getShips()) {
            if (ship != null) {
                for (ShipPoint shipPoint : ship.getShipPoints()) {
                    if (shipPoint.equals(point)) {
                        shipPoint.setShot(true);
                    }
                }
            }

        }

    }

    public boolean checkSunkShip(Stage stage) {
        int shotsInShip;
        for (Ship ship : stage.getShips()) {
            shotsInShip = 0;
            if (ship.isAlive()) {
                for (ShipPoint shipPoint : ship.getShipPoints()) {
                    if (shipPoint.isShot()) {
                        shotsInShip++;
                    }
                }
                if (shotsInShip == ship.getLength()) {
                    ship.setAlive(false);
                    return true;
                }
            }
        }
        return false;
    }



    public boolean checkEndGame(Stage stage) {
        for (Ship ship : stage.getShips()) {
                if (ship.isAlive()) {return false;}
        }
        return true;
    }


    public void game(Stage[] stages, int playerNumber) {
        showStage(stages[0], true);
        System.out.println("---------------------");
        showStage(stages[playerNumber], false);
        System.out.println("Player " + playerNumber + ", it's your turn:");
        int otherStage = playerNumber == 1 ? 2 : 1;
        while (true) {
            try {
                Point shotCoordinate = getCoordinate(scanner);
                if (!pointIsShip(stages[otherStage], shotCoordinate)) {
                    stages[otherStage].getHits()[shotCoordinate.x][shotCoordinate.y] = true;
                    System.out.println("You missed!");
                    break;
                } else {
                    shotShip(stages[otherStage], shotCoordinate);
                    showStage(stages[otherStage],  true);
                    if (checkSunkShip(stages[otherStage]) && !checkEndGame(stages[otherStage])) {
                        System.out.println("You sank a ship! Specify a new target:");
                        break;
                    } else if (checkEndGame(stages[otherStage])) {
                        System.out.println("You sank the last ship. You won. Congratulations!");
                        setGameOn(false);
                        break;
                    } else {
                        System.out.println("You hit a ship!");
                        break;
                    }
                }

            } catch (Exception e) {
                System.out.println("Error, coordinate must have the following format: XN, X = A - J, N = 1 - 10.");
                scanner.nextLine();
            }
        }
    }

    public void passToAnotherPlayer() {
            System.out.println("Press Enter and pass the move to another player");
            scanner.nextLine();
            String readString = scanner.nextLine();
        }

    public boolean isGameOn() {
        return gameOn;
    }

    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }
}

public class Main {

    public static void main(String[] args) {
    Stage[] stages = new Stage[3];
    stages[0] = new Stage();
    stages[1] = new Stage();
    stages[2] = new Stage();
	Scanner scanner = new Scanner(System.in);
	BattleShipController battleShipController = new BattleShipController(scanner);
	battleShipController.populateStage(stages[1], 1);
    battleShipController.passToAnotherPlayer();
    battleShipController.populateStage(stages[2], 2);
    battleShipController.passToAnotherPlayer();
    while (battleShipController.isGameOn()) {
        battleShipController.game(stages, 1);
        if (!battleShipController.isGameOn()) {break;}
        battleShipController.passToAnotherPlayer();
        battleShipController.game(stages, 2);
        if (!battleShipController.isGameOn()) {break;}
        battleShipController.passToAnotherPlayer();
    }

    }
}
