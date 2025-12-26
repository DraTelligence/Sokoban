package controller;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import view.panels.MainMenuPanel;

public class MainMenuController {
    private static MainMenuController instance;
    private final MainMenuPanel view;

    private MainMenuController() {
        view = new MainMenuPanel();

        view.addMenuListener((ActionEvent e) -> {
            handleMenuAction(((JButton) e.getSource()).getText());
        });
    }

    public MainMenuPanel getView() {
        return view;
    }

    public static synchronized MainMenuController getInstance() {
        if (instance == null) {
            instance = new MainMenuController();
        }
        return instance;
    }

    public void handleMenuAction(String action) {
        switch(action){
            case"start"->AppController.getInstance().switchToLevelSelectMenu();
            case"exit"->AppController.getInstance().exitProgram();
            case"user"->AppController.getInstance().switchToUserSystem();
            // case""
        }
    }
}
