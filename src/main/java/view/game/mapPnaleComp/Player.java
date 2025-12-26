package view.game.mapPnaleComp;

import java.awt.Image;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

import model.game.Direction;

public class Player extends JLabel {
    private static enum Direc {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

        private final int col, row;

        private Direc(int col, int row) {
            this.col = col;
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public int getRow() {
            return row;
        }

        @Override
        public String toString() {
            return switch (this) {
                case UP -> "backward";
                case DOWN -> "forward";
                case LEFT -> "leftward";
                case RIGHT -> "rightward";
                default -> "forward";
            };
        }
    }

    private Direc currDirec;
    final private static int SIZE = 45;
    final private static int DELAY = 25;
    private String state;
    private int currCol, currRow;
    private int preCol, preRow;
    private int totShift;
    private final Stack<Direction> motions;

    public Player(int initCol, int initRow) {
        currDirec = Direc.DOWN;
        this.setBounds(initCol * SIZE, initRow * SIZE, SIZE, SIZE);
        this.state = "still";
        this.setCorrespondingIcon();
        currCol = initCol;
        currRow = initRow;
        motions = new Stack<>();

        this.setVisible(true);
    }

    private String getUrl() {
        StringBuilder sb = new StringBuilder();

        sb.append("icons\\Player\\").append(this.currDirec).append("_").append(this.state).append(".png");

        return sb.toString();
    }

    private void setCorrespondingIcon() {
        java.net.URL imgURL = getClass().getClassLoader().getResource(getUrl());
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            icon.setImage(icon.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_DEFAULT));
            this.setIcon(icon);
        } else {
            System.err.println("Couldn't find file: " + getUrl());
        }
    }

    @SuppressWarnings("unused")
    private void move(Direc dir) {
        preRow = currRow;
        preCol = currCol;

        currRow += dir.getRow();
        currCol += dir.getCol();
        this.setCorrespondingIcon();
        this.setLocation(preCol * SIZE, preRow * SIZE);

        totShift = 0;

        this.currDirec = dir;

        Timer trigger = new Timer(DELAY, (var noUse) -> {
            if (totShift <= 40) {
                totShift += 6;
                state = String.format("walking_%d", totShift / 2 % 2 + 1);
                this.setCorrespondingIcon();
                this.setLocation(preCol * SIZE + totShift * dir.getCol(), preRow * SIZE + totShift * dir.getRow());
            } else if (totShift <= 100) {
                state = "still";
                this.setCorrespondingIcon();
                this.setLocation(currCol * SIZE, currRow * SIZE);
                totShift = 101;
            }

            repaint();
        });
        Timer timer = new Timer(300, (var Nouse) -> {
            trigger.stop();
        });
        timer.setRepeats(false);

        trigger.start();
        timer.start();
    }

    @SuppressWarnings("unused")
    private void moveRewind(Direc dir) {
        preRow = currRow;
        preCol = currCol;

        currRow -= dir.getRow();
        currCol -= dir.getCol();
        this.setCorrespondingIcon();
        this.setLocation(preCol * SIZE, preRow * SIZE);

        totShift = 0;

        this.currDirec = dir;

        Timer trigger = new Timer(DELAY, (var noUse) -> {
            if (totShift <= 40) {
                totShift += 6;
                state = String.format("walking_%d", totShift / 2 % 2 + 1);
                this.setCorrespondingIcon();
                this.setLocation(preCol * SIZE - totShift * dir.getCol(), preRow * SIZE - totShift * dir.getRow());
            } else if (totShift <= 100) {
                state = "still";
                this.setCorrespondingIcon();
                this.setLocation(currCol * SIZE, currRow * SIZE);
                totShift = 101;
            }

            repaint();
        });
        Timer timer = new Timer(300, (var Nouse) -> {
            trigger.stop();
        });
        timer.setRepeats(false);

        trigger.start();
        timer.start();
    }

    @SuppressWarnings("unused")
    private void cannotMove(Direc dir) {
        totShift = 0;

        this.currDirec = dir;

        Timer trigger = new Timer(DELAY, (var noUse) -> {
            totShift += 1;

            state = "still";
            this.setCorrespondingIcon();
            int[] shifts = { 0, 8, 12, 14, 16, 17, 9, 2, -2, -2, 0 };

            if (totShift <= 10) {
                this.setLocation(currCol * SIZE + dir.getCol() * shifts[totShift],
                        currRow * SIZE + dir.getRow() * shifts[totShift]);
            }

            repaint();
        });
        Timer timer = new Timer(500, (var Nouse) -> {
            trigger.stop();
        });
        timer.setRepeats(false);

        trigger.start();
        timer.start();
    }

    public void move(Direction dir) {
        switch (dir) {
            case DOWN -> move(Direc.DOWN);
            case LEFT -> move(Direc.LEFT);
            case RIGHT -> move(Direc.RIGHT);
            case UP -> move(Direc.UP);
        }

        motions.push(dir);
    }

    public void rewind() {
        switch (motions.pop()) {
            case DOWN -> moveRewind(Direc.DOWN);
            case LEFT -> moveRewind(Direc.LEFT);
            case RIGHT -> moveRewind(Direc.RIGHT);
            case UP -> moveRewind(Direc.UP);
        }
    }

    public void move_failed(Direction dir) {
        switch (dir) {
            case DOWN -> cannotMove(Direc.DOWN);
            case LEFT -> cannotMove(Direc.LEFT);
            case RIGHT -> cannotMove(Direc.RIGHT);
            case UP -> cannotMove(Direc.UP);
        }
    }

    public int getCurrCol() {
        return currCol;
    }

    public int getCurrRow() {
        return currRow;
    }
}