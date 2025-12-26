package controller;

import javax.swing.JPanel;

import view.panels.LevelSelectMenuPanel;

public class LevelSelectMenuController {
    private static LevelSelectMenuController instance;

    private final LevelSelectMenuPanel view= new LevelSelectMenuPanel();

    private LevelSelectMenuController() {
        System.out.println("select:");
    }

    public void select(int levelNum){
        AppController.getInstance().initGame(levelNum);
        AppController.getInstance().switchToGameController();
    }

    public JPanel getView() {
        return view;
    }

    public static synchronized LevelSelectMenuController getInstance() {
        if (instance == null) {
            instance = new LevelSelectMenuController();
        }
        return instance;
    }
}
