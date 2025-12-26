package controller;

import javax.swing.JPanel;

import model.exceptions.PswdIncorrectException;
import model.exceptions.UserAlreadyExistsException;
import model.exceptions.UserNotFoundException;
import model.user.UserSystem;
import view.panels.UserSystemPanel;

public class UserSystemController {
    private static UserSystemController instance;

    private static final UserSystemPanel view = UserSystemPanel.getInstance();
    private final UserSystem model = new UserSystem();

    public JPanel getView() {
        return view;
    }

    public static synchronized UserSystemController getInstance() {
        if (instance == null) {
            instance = new UserSystemController();
        }
        return instance;
    }

    public void signUp(String userName, String password) throws UserAlreadyExistsException {
        model.signUp(userName, password);
    }

    public void logIn(String userName, String password) throws UserNotFoundException, PswdIncorrectException {
        model.logIn(userName, password);
    }

    /**
     * the method will be called by view to get the name of the current user from
     * model
     * 
     * @return
     */
    public String getUserName() {
        if(model.checkLoggedIn()){
            return model.getCurrentUser();
        }else{
            return null;
        }
    }

    public String getStageInfo(){
        if(model.checkLoggedIn()){
            return model.getStageInfo();
        }else{
            return "";
        }
    }

    public void setStageInfo(int stageID, int steps, int time){
        model.updateStageInfo(stageID, steps, time);
    }

    /**
     * the method will be called by view to get the password of the current user
     * from model
     */
    public void init() {
        if (model.checkLoggedIn()) {
            view.updatePanel();
            view.switchPanel("profile");
        }else{
            view.switchPanel("logIn");
        }
    }
}
