package model.game;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Scanner;
import java.util.Stack;

import controller.GameController;
import model.game.autosolver.AutoSolver;
import view.game.mapPnaleComp.StaticMapComponent;

/**
 * The Map class represents the map of the game. It contains a matrix of
 * mapComponents.
 */

@SuppressWarnings("FieldMayBeFinal")
public class Map implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Serial
    private MapComponents[][] map;

    @Serial
    private int posX, posY;

    transient private Stack<Map> record;
    static transient private boolean hintReady = false;
    static transient private Deque<Map> hints = new ArrayDeque<>();
    static final int PRIME_FOR_HASHCODE = 31;

    public Map() {
        this.record = new Stack<>();
    }

    private Map(MapComponents[][] map, int posx, int posy) {
        this.map = map;
        this.posX = posx;
        this.posY = posy;
    }

    public void initMap(int levelNum) {
        readMapFromFile(levelNum);
    }

    @Override
    public Map clone(){
        return new Map(this.getMapComponentsMatrix(),this.getPlayerPosX(),this.getPlayerPosY());
    }

    /**
     * This method return a {@code Map} read from data file
     * 
     * @param levelNum
     * @return
     */
    private void readMapFromFile(int levelNum) {
        int width, height;

        try {
            InputStream inp = getClass().getClassLoader().getResourceAsStream("maps/level" + levelNum);
            if (inp == null) {
                throw new FileNotFoundException("Map file not found: level" + levelNum);
            }

            // read the size of the map
            try (Scanner sc = new Scanner(inp)) {
                // read the size of the map
                height = sc.nextInt();
                width = sc.nextInt();

                // read the map
                this.map = new MapComponents[height][width];
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        this.map[i][j] = MapComponents.valueOf(sc.nextInt());
                    }
                }

                // read the initial pos of the player
                this.posY = sc.nextInt();
                this.posX = sc.nextInt();
            }
        } catch (FileNotFoundException e) {
            System.out.print(e.getMessage());
            System.exit(-1);
        }
    }

    // Serialize the object and include a hash of the data
    public void serializeWithHash(String filename, ObjectOutputStream oos)
            throws NoSuchAlgorithmException, IOException {
        // Serialize the object
        oos.writeObject(this);

        // Compute the hash of the serialized object
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream tempOos = new ObjectOutputStream(baos);
        tempOos.writeObject(this);
        byte[] objectBytes = baos.toByteArray();
        byte[] hash = computeHash(objectBytes);

        // Write the hash to the file
        oos.writeObject(hash);
    }

    // Deserialize the object and verify the hash
    public static Map deserializeWithHash(String filename, ObjectInputStream ois)
            throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        // Deserialize the object
        Map map = (Map) ois.readObject();

        // Read the stored hash
        byte[] storedHash = (byte[]) ois.readObject();

        // Compute the hash of the deserialized object
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream tempOos = new ObjectOutputStream(baos);
        tempOos.writeObject(map);
        byte[] objectBytes = baos.toByteArray();
        byte[] computedHash = computeHash(objectBytes);

        // Verify the hash
        if (!Arrays.equals(storedHash, computedHash)) {
            throw new SecurityException("Data integrity check failed. The file may have been tampered with.");
        }

        return map;
    }

    // Compute the hash of the given data
    private static byte[] computeHash(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(data);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = PRIME_FOR_HASHCODE * result + Arrays.deepHashCode(map);
        result = PRIME_FOR_HASHCODE * result + posX;
        result = PRIME_FOR_HASHCODE * result + posY;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Map other = (Map) obj;

        return Arrays.deepEquals(map, other.map) || posX != other.posX || posY != other.posY;
    }

    /**
     * Checks if the specified position is within the bounds of the map.
     * 
     * @param row the row index
     * @param col the column index
     * @return true if the position is valid, false otherwise
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < getHeight() && col >= 0 && col < getWidth();
    }

    /**
     * Returns the map component at the specified offset from the current position.
     * 
     * @param offX the offset in the X direction
     * @param offY the offset in the Y direction
     * @return the map component at the specified offset
     */
    private MapComponents get(int offX, int offY) {
        if (isValidPosition(this.posY + offY, this.posX + offX)) {
            return this.map[this.posY + offY][this.posX + offX];
        }
        throw new IndexOutOfBoundsException("Invalid position");
    }

    /**
     * Returns the map component in the specified direction from the current
     * position.
     * 
     * @param direction the direction to move
     * @return the map component in the specified direction
     */
    private MapComponents get(Direction direction) {
        int newRow = this.posY + direction.getRow();
        int newCol = this.posX + direction.getCol();
        if (isValidPosition(newRow, newCol)) {
            return this.map[newRow][newCol];
        }
        throw new IndexOutOfBoundsException("Invalid position");
    }

    /**
     * Returns a new StaticMapComponent at the specified position and stage number.
     * 
     * @param col      the column index
     * @param row      the row index
     * @param stageNum the stage number
     * @return a new StaticMapComponent
     */
    public StaticMapComponent getComp(int col, int row, int stageNum) {
        if (isValidPosition(row, col)) {
            return new StaticMapComponent(this.map[row][col], stageNum, col, row);
        }
        throw new IndexOutOfBoundsException("Invalid position");
    }

    /**
     * Returns the map components matrix.
     * 
     * @return the map components matrix
     */
    public MapComponents[][] getMapComponentsMatrix() {
        return map;
    }

    /**
     * Returns the width of the map.
     * 
     * @return the width of the map
     */
    public int getWidth() {
        return this.map[0].length;
    }

    /**
     * Returns the height of the map.
     * 
     * @return the height of the map
     */
    public int getHeight() {
        return this.map.length;
    }

    public int getPlayerPosX() {
        return posX;
    }

    public int getPlayerPosY() {
        return posY;
    }

    @Override
    public String toString() {
        System.out.printf("this is map %d:\n", hashCode());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                if (j == this.posX && i == this.posY) {
                    sb.append("$").append(" ");
                } else {
                    sb.append(this.map[i][j].ordinal()).append(" ");
                }
            }
            sb.append("\n");
        }

        sb.append("player currently at (").append(this.posX).append(",").append(this.posY).append(")\n");

        return sb.toString();
    }

    /**
     * This method check if the move can be done.
     *
     * @param map
     * @param direction
     * @return {@code true} if the move can be done.
     */
    public static boolean checkMove(Map map, Direction direction) {
        boolean flag = false;

        if (MapComponents.isStandable(map.get(direction))) {
            flag = true;
        } else if (MapComponents.isPushable(map.get(direction))) {
            if (MapComponents.isStandable(map.get(direction.getCol() * 2, direction.getRow() * 2))) {
                flag = true;
            }
        }

        return flag;
    }

    /**
     * Complete the move and return the Map after the move.
     * Warning: This method should only be called when the move could be done
     * 
     * @param currMap
     * @param direction
     * @return the {@code Map} after the move done.
     */
    public static Map doMove(Map currMap, Direction direction) {
        int newX = currMap.getPlayerPosX() + direction.getCol();
        int newY = currMap.getPlayerPosY() + direction.getRow();

        if (MapComponents.isPushable(currMap.get(direction))) {
            // the move will push a crate
            int height = currMap.getHeight();
            int width = currMap.getWidth();
            MapComponents[][] newMap = new MapComponents[height][width];
            for (int i = 0; i < height; i++) {
                newMap[i] = currMap.getMapComponentsMatrix()[i].clone();
            }

            // push the crate
            if (currMap.get(direction.getCol() * 2, direction.getRow() * 2) == MapComponents.SPACE) {
                // space
                newMap[newY + direction.getRow()][newX + direction.getCol()] = MapComponents.BOX;
            } else {
                // target
                newMap[newY + direction.getRow()][newX + direction.getCol()] = MapComponents.BOX_ON_TARGET;
            }

            // reset the ground
            if (currMap.get(direction) == MapComponents.BOX) {
                // space
                newMap[newY][newX] = MapComponents.SPACE;
            } else {
                // target
                newMap[newY][newX] = MapComponents.TARGET;
            }

            return new Map(newMap, newX, newY);
        } else {
            return new Map(currMap.getMapComponentsMatrix(), newX, newY);
        }
    }

    public void doMove(Direction direction) {
        if (checkMove(this, direction)) {
            hintReady = false;
            this.record.push(this.clone());
            Map newMap = doMove(this, direction);

            this.map = newMap.getMapComponentsMatrix();
            this.posX = newMap.getPlayerPosX();
            this.posY = newMap.getPlayerPosY();

            GameController.getInstance().updateView(direction, "move");

            if (checkVictory(this)) {
                GameController.getInstance().showVictory();
            }
        } else {
            GameController.getInstance().updateView(direction, "fail");
        }
    }

    /**
     * this version of doMove is used by the showHint method
     */
    private void doMove(Map nextMap) {
        this.map = nextMap.getMapComponentsMatrix();
        this.posX = nextMap.getPlayerPosX();
        this.posY = nextMap.getPlayerPosY();
        GameController.getInstance().updateView(getNextStep(this, nextMap), "move");
    }

    public void undoMove() {
        if (!this.record.isEmpty()) {
            Map lastMap = this.record.pop();
            this.map = lastMap.getMapComponentsMatrix();
            this.posX = lastMap.getPlayerPosX();
            this.posY = lastMap.getPlayerPosY();
            GameController.getInstance().updateView(null, "rewind");
        }
    }

    static public void produceHint(ArrayList<Map> hint) {
        hintReady = true;

        if (hint == null) {
            System.out.println("No solution found");
        } else {
            for (int i = hint.size() - 1; i > 0; i--) {
                hints.push(hint.get(i));
            }
        }

        GameController.getInstance().showLoadingDone();
    }

    /**
     * GameController will call this method to show the next hint step. If the hint
     * isn't ready yet, the method will call AutoSolver to produce hints. Otherwise,
     * it will show the next hint.
     */
    public void showHint() {
        if (!hintReady) {
            new AutoSolver(this).execute();
            GameController.getInstance().showLoading();
        } else {
            this.doMove(hints.pop());
        }
    }

    /**
     * This function is to check if the map has arrived the victory state
     *
     * @param map the map to be checked
     * @return {@code true} if the player has won
     */
    static public boolean checkVictory(Map map) {
        boolean flag = true;

        for (int y = 1; y < map.getMapComponentsMatrix().length - 1; y++) {
            for (int x = 1; x < map.getMapComponentsMatrix()[0].length - 1; x++) {
                if (map.getMapComponentsMatrix()[y][x] == MapComponents.BOX) {
                    flag = false;
                }
            }
        }

        return flag;
    }

    static public Direction getNextStep(Map preMap, Map postMap) {
        int dx = postMap.getPlayerPosX() - preMap.getPlayerPosX();
        int dy = postMap.getPlayerPosY() - preMap.getPlayerPosY();

        return switch (dx) {
            case -1 -> Direction.LEFT;
            case 1 -> Direction.RIGHT;
            default -> switch (dy) {
                case -1 -> Direction.UP;
                case 1 -> Direction.DOWN;
                default -> null;
            };
        };
    }
}
