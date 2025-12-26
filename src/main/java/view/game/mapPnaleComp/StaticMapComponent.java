package view.game.mapPnaleComp;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import model.game.MapComponents;

@SuppressWarnings("FieldMayBeFinal")
public class StaticMapComponent extends JLabel {
    static private final int SIZE = 45;
    private MapComponents mapComponent;
    private final int firstFolderNum;

    public StaticMapComponent(MapComponents mapComponent, int stageNum, int col, int row) {
        this.mapComponent = mapComponent;
        this.firstFolderNum = stageNum % 3;

        this.setBounds(0, 0, 45, 45);

        setIcon();
    }

    private String getUrl() {
        StringBuilder sb = new StringBuilder();

        sb.append("icons\\");
        switch (mapComponent) {
            case BOX, SPACE -> sb.append("Ground\\ground").append(firstFolderNum).append("\\ground.png");
            case BOX_ON_TARGET, TARGET ->
                sb.append("Ground\\ground").append(firstFolderNum).append("\\ground_with_target.png");
            case WALL, VOID -> sb.append("Wall\\wall0.png");
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
