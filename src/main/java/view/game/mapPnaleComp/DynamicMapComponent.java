package view.game.mapPnaleComp;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import model.game.MapComponents;

@SuppressWarnings("FieldMayBeFinal")
public class DynamicMapComponent extends JLabel {
    static private final int SIZE = 45;
    private MapComponents mapComponent;
    private final int firstFolderNum, secondFolderNum;

    public DynamicMapComponent(MapComponents mapComponent, int stageNum, int col, int row) {
        this.mapComponent = mapComponent;
        this.firstFolderNum = stageNum % 3;
        this.secondFolderNum = stageNum % 2;

        this.setBounds(SIZE * col, SIZE * row, SIZE, SIZE);

        setIcon();
    }

    private String getUrl() {
        StringBuilder sb = new StringBuilder();

        sb.append("icons\\");
        switch (mapComponent) {
            case BOX -> sb.append("Crates\\crates").append(firstFolderNum).append("\\crate").append(secondFolderNum)
                    .append(".png");
            case BOX_ON_TARGET -> sb.append("Crates\\crates").append(firstFolderNum).append("\\crate")
                    .append(secondFolderNum).append(".png");
            case SPACE -> sb.append("Ground\\ground").append(firstFolderNum).append("ground.png");
            case TARGET -> sb.append("Ground\\ground").append(firstFolderNum).append("ground_with_target.png");
            case WALL -> sb.append("Wall\\wall0.png");
            case VOID -> sb.append("Wall\\wall0.png");
        }
        return sb.toString();
    }

    public void setMapComponent(MapComponents component) {
        this.mapComponent = component;
        setIcon();

        repaint();
    }

    private void setIcon() {
        java.net.URL imgURL = getClass().getClassLoader().getResource(getUrl());
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            icon.setImage(icon.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_DEFAULT));
            this.setIcon(icon);
        } else {
            System.err.println("Couldn't find file: " + getUrl());
        }
    }
}
