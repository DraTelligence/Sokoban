package controller;

import java.awt.event.ActionEvent;

import javax.security.auth.login.AppConfigurationEntry;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import model.game.Direction;
import model.game.Map;
import view.app.ContentDialog;
import view.panels.GameMainPanel;

/**
 * It is a bridge to combine GamePanel(view) and MapMatrix(model) in one game.
 * You can design several methods about the game logic in this class.
 */
public class GameController {
    private static GameController instance;

    private final GameMainPanel view;
    private final Map model;

    private GameController() {
        this.view = new GameMainPanel();
        this.model = new Map();
    }

    public static synchronized GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public GameMainPanel getView() {
        return view;
    }

    /**
     * The AppController will call this method to initialize the game, show the game panel.
     * @param levelNum
     */
    public void initGame(int levelNum) {
        this.model.initMap(levelNum);
        this.view.initPanel(this.model);
    }

    public void handleCommand(String command) {
        switch(command) {
            case "moveLeft" -> this.model.doMove(Direction.LEFT);
            case "moveDown" -> this.model.doMove(Direction.DOWN);
            case "moveRight" -> this.model.doMove(Direction.RIGHT);
            case "moveUp" -> this.model.doMove(Direction.UP);
            case "getHint" -> this.model.showHint();
            case "doRewind" -> this.model.undoMove();
        }
    }

    public void showVictory(){
        view.showVictory();
        Timer timer=new Timer(2000,(ActionEvent e)->{
            new ContentDialog("win!").setVisible(true);
            AppController.getInstance().switchToMainMenu();
        });
        timer.setRepeats(false);
        timer.start();
        //UserSystemController.getInstance().setStageInfo(stageID, steps, time);
        

    }

    /**
     * the model will call this method to inform the view to perform loading.
     */
    public void showLoading(){
        view.showLoading();
    }

    public void showLoadingDone(){
        view.showLoadingDone();
    }

    public void restartGame() {
        System.out.println("Do restart game here");
    }

    public void updateView(Direction direction, String command) {
        switch(command){
            case "move" -> view.doMove(direction);
            case "fail" -> view.doMoveFail(direction);
            case "rewind" -> view.doRewind();
        }
    }
}

