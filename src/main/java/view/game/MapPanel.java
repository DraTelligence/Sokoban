package view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import model.game.Direction;
import model.game.Map;
import model.game.MapComponents;
import view.game.mapPnaleComp.Crate;
import view.game.mapPnaleComp.OutOfMapWalls;
import view.game.mapPnaleComp.Player;

public class MapPanel extends JLayeredPane {
    private static final int MAX_COL = 11;
    private static final int MAX_ROW = 9;
    private static final Point BASE_POINT = new Point(-22, 129);
    private static final int GRID_SIZE = 45;
    private static final OutOfMapWalls[][] BLANK_MAP = readInitialMap();

    private final DynamicLayer dynamicLayer;
    private final StaticLayer staticLayer;

    private int col, row, startCol, startRow;

    public MapPanel() {
        this.setLocation(BASE_POINT);
        this.setSize(new Dimension(585, 495));
        this.setOpaque(false);

        staticLayer = new StaticLayer();
        dynamicLayer = new DynamicLayer();

        this.add(staticLayer, 1);
        this.add(dynamicLayer, 0);
    }

    public void initPanel(Map map) {
        this.col = map.getWidth();
        this.row = map.getHeight();
        this.startCol = (MAX_COL - col) / 2;
        this.startRow = (MAX_ROW - row) / 2;

        dynamicLayer.initLayer(map);
        staticLayer.initLayer(map);
    }

    /**
     * this Layer is to put unchangable elements: ground, walls, etc.
     */
    private class StaticLayer extends JPanel {
        public StaticLayer() {
            this.setLocation(BASE_POINT);
            this.setSize(new Dimension(585, 495));
            this.setBackground(new Color(0, 0, 0, 100));
            this.setLayout(new GridBagLayout());
        }

        public void initLayer(Map map) {
            this.removeAll();

            var layoutConstraints = new GridBagConstraints();

            // init out of map comps
            for (int i = 0; i < MAX_ROW + 2; i++) {
                for (int j = 0; j < MAX_COL + 2; j++) {
                    if (i >= startRow + 1 && i <= startRow + row) {
                        if (j >= startCol + 1 && j <= startCol + col) {
                            continue;
                        }
                    }

                    layoutConstraints.gridx = j;
                    layoutConstraints.gridy = i;
                    layoutConstraints.gridwidth = 1;
                    layoutConstraints.gridheight = 1;

                    this.add(BLANK_MAP[i][j], layoutConstraints);
                }
            }

            // init map comps
            JPanel mainMap = new JPanel(new GridLayout(row, col));
            mainMap.setBounds(0, 0, GRID_SIZE * col, GRID_SIZE * row);
            mainMap.setBackground(new Color(156, 169, 169));

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    mainMap.add(map.getComp(j, i, 2));
                }
            }

            mainMap.setVisible(true);

            layoutConstraints.gridx = startCol + 1;
            layoutConstraints.gridy = startRow + 1;
            layoutConstraints.gridwidth = col;
            layoutConstraints.gridheight = row;
            this.add(mainMap, layoutConstraints);

            this.revalidate();
            this.repaint();
        }
    }

    /**
     * this layer is to show dynamic elements: player, boxes, etc.
     */
    private class DynamicLayer extends JPanel {
        Player player;
        ArrayList<Crate> crates;

        public DynamicLayer() {
            this.setLocation(BASE_POINT);
            this.setSize(new Dimension(585, 495));
            this.setLayout(null);
            this.setOpaque(false);
        }

        public void initLayer(Map map) {
            this.removeAll();
            if (crates != null) {
                crates.clear();
            }

            player = new Player(1 + map.getPlayerPosX() + startCol, 1 + map.getPlayerPosX() + startRow);
            crates = new ArrayList<>();

            this.add(player);
            var tempMap = map.getMapComponentsMatrix();
            for (int i = 1; i < row - 1; i++) {
                for (int j = 1; j < col - 1; j++) {
                    if (tempMap[i][j] == MapComponents.BOX || tempMap[i][j] == MapComponents.BOX_ON_TARGET) {
                        crates.add(new Crate(startCol + j + 1, startRow + i + 1));
                    }
                }
            }
            for (var crate : crates) {
                this.add(crate);
            }

            this.revalidate();
            this.repaint();
        }

        protected void showVictory(){
            for(var crate: crates){
                crate.showVictory();
            }
        }

        protected void doMove(Direction dir) {
            for (var crate : crates) {
                if (crate.getCurrCol() == player.getCurrCol() + dir.getCol() &&
                        crate.getCurrRow() == player.getCurrRow() + dir.getRow()) {
                    crate.move(dir);
                } else {
                    crate.move(null);
                }
            }

            player.move(dir);
        }

        protected void doRewind() {
            for (var crate : crates) {
                crate.rewind();
            }

            player.rewind();
        }

        protected void move_failed(Direction dir) {
            player.move_failed(dir);
        }
    }

    private static OutOfMapWalls[][] readInitialMap() {
        OutOfMapWalls[][] result = new OutOfMapWalls[MAX_ROW + 2][MAX_COL + 2];

        try {
            String dir = "/maps/blankLevel.txt";
            InputStream inp = MapPanel.class.getResourceAsStream(dir);
            if (inp == null) {
                throw new FileNotFoundException("Resource not found: " + dir);
            }
            // read the map
            try (Scanner sc = new Scanner(inp)) {
                // read the map
                for (int i = 0; i < MAX_ROW + 2; i++) {
                    for (int j = 0; j < MAX_COL + 2; j++) {
                        result[i][j] = new OutOfMapWalls(sc.nextInt(), j, i);
                    }
                }
            }

            return result;
        } catch (FileNotFoundException e) {
            System.out.print(e.getMessage());
            System.exit(-1);
        }

        return result;
    }

    public void showVictory(){
        this.dynamicLayer.showVictory();
    }

    public void doMove(Direction dir) {
        dynamicLayer.doMove(dir);
    }

    public void doRewind() {
        dynamicLayer.doRewind();
    }

    public void doMoveFail(Direction dir) {
        dynamicLayer.move_failed(dir);
    }
}
