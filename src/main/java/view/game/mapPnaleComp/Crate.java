package view.game.mapPnaleComp;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

import model.game.Direction;

public class Crate extends JLabel {
    final private static int SIZE = 45;
    final private static int DELAY = 25;
    final private long crateNum;
    @SuppressWarnings("FieldMayBeFinal")
    private String state;
    private int currCol, currRow;
    private int preCol, preRow;
    private int totShift;
    private final Stack<Direction> motions;
    private ImageIcon dimIcon, brightIcon;
    private float alpha = 0.0f;

    public Crate(int initCol, int initRow) {
        this.crateNum = (System.currentTimeMillis() % 4) + 1;
        this.setBounds(initCol * SIZE, initRow * SIZE, SIZE, SIZE);
        this.state = "";
        this.setCorrespondingIcon();
        currCol = initCol;
        currRow = initRow;
        motions = new Stack<>();

        java.net.URL imgURL = getClass().getClassLoader().getResource(getUrl());
        if (imgURL != null) {
            dimIcon = new ImageIcon(imgURL);
            dimIcon.setImage(dimIcon.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_DEFAULT));
        }
        java.net.URL victoryImgURL = getClass().getClassLoader().getResource(getVictoryUrl());
        if (imgURL != null) {
            brightIcon = new ImageIcon(victoryImgURL);
            brightIcon.setImage(brightIcon.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_DEFAULT));
        }

        this.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.drawImage(dimIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(brightIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        g2d.dispose();
    }

    private String getUrl() {
        StringBuilder sb = new StringBuilder();

        sb.append("icons\\Crates\\crates").append(crateNum);
        sb.append("\\crate").append(state).append(".png");

        return sb.toString();
    }

    private String getVictoryUrl() {
        StringBuilder sb = new StringBuilder();

        sb.append("icons\\Crates\\crates").append(crateNum);
        sb.append("\\crate_onTarget").append(state).append(".png");

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

    // animation should last about 250 milsec
    @SuppressWarnings("unused")
    public void move(Direction dir) {
        if (dir == null) {
            motions.push(null);
            return;
        }

        motions.push(dir);

        totShift = 0;

        preCol = currCol;
        preRow = currRow;

        currRow += dir.getRow();
        currCol += dir.getCol();
        this.setLocation(preCol * SIZE, preRow * SIZE);

        Timer trigger = new Timer(DELAY, (var noUse) -> {
            totShift += 6;

            if (totShift <= 45) {
                this.setLocation(preCol * SIZE + totShift * dir.getCol(), preRow * SIZE + totShift * dir.getRow());
            } else if (totShift <= 100) {
                this.setLocation(currCol * SIZE, currRow * SIZE);
                this.totShift = 101;
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
    private void rewind(Direction dir) {
        totShift = 0;

        preCol = currCol;
        preRow = currRow;

        currRow -= dir.getRow();
        currCol -= dir.getCol();
        this.setLocation(preCol * SIZE, preRow * SIZE);

        Timer trigger = new Timer(DELAY, (var noUse) -> {
            totShift += 6;

            if (totShift <= 45) {
                this.setLocation(preCol * SIZE - totShift * dir.getCol(), preRow * SIZE - totShift * dir.getRow());
            } else if (totShift <= 100) {
                this.setLocation(currCol * SIZE, currRow * SIZE);
                this.totShift = 101;
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

    public void rewind() {

        if (motions.peek() != null) {
            switch (motions.pop()) {
                case DOWN -> rewind(Direction.DOWN);
                case LEFT -> rewind(Direction.LEFT);
                case RIGHT -> rewind(Direction.RIGHT);
                case UP -> rewind(Direction.UP);
            }
        } else {
            motions.pop();
        }
    }

    public void showVictory() {
        Timer timer = new Timer(50, (ActionEvent e1) -> {
            alpha += 0.05f;
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                ((Timer) e1.getSource()).stop();
            }
        });
        timer.start();
    }

    public int getCurrCol() {
        return currCol;
    }

    public int getCurrRow() {
        return currRow;
    }
}
