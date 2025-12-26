package model.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import model.exceptions.PswdIncorrectException;
import model.exceptions.UserAlreadyExistsException;
import model.exceptions.UserNotFoundException;
import model.game.Map;
import view.app.ContentDialog;

/**
 * This class produces the accesse to manage users. Users should only be visited
 * via this class's methods.
 */

public class UserSystem {
    private User currentUser= null;

    /**
     * Produce the path of saves
     * C://...//%user%//AppData//Roaming//ThereIsA_Studio//Sokoban//saves
     *
     * @return The path of the dir where saves are stored
     */
    private static Path getSavePath() {
        Path savePath;

        String rootFolder = "C:\\Users\\";
        String userFolder = System.getProperty("user.name");
        String saveFolder = "\\AppData\\Roaming\\ThereIsA_Studio\\Sokoban\\saves\\";

        savePath = Paths.get(rootFolder + userFolder + saveFolder);

        return savePath;
    }

    /**
     * To sign up for the user
     *
     * @param userName
     * @param pswd
     * @throws UserAlreadyExistsException
     */
    @SuppressWarnings("ConvertToTryWithResources")
    public void signUp(String userName, String pswd) throws UserAlreadyExistsException {
        try {
            Files.createDirectories(getSavePath());
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }

        File directory = getSavePath().toFile();
        File[] fileList = directory.listFiles();

        // check if the user name has been occupied
        if (fileList != null) {
            for (File f : fileList) {
                if (f.getName().equals(userName + ".ser")) {
                    throw new UserAlreadyExistsException();
                }
            }
        }

        // sign up for the user
        this.currentUser = new User(userName, pswd);

        // create an archieve file for the user
        save();
    }

    /**
     * write the current user to file 
     */
    private void save(){
        try {
            FileOutputStream fileOut = new FileOutputStream(getSavePath().toFile() + "\\" + currentUser.getUserName() + ".ser");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            currentUser.serializeWithHash(objectOut);

            objectOut.close();
            fileOut.close();
        } catch (IOException | NoSuchAlgorithmException e) {
            new ContentDialog("error occur here because: "+e.getMessage()).setVisible(true);
        }
    }

    /**
     * To log the user in, throws a exception if encounters any problem.
     *
     * @param userName
     * @param pswd
     * @throws PswdIncorrectException
     * @throws UserNotFoundException
     */
    public void logIn(String userName, String pswd)
            throws PswdIncorrectException, UserNotFoundException {
        File directory = getSavePath().toFile();
        File[] fileList = directory.listFiles();

        FileInputStream fin;
        ObjectInputStream oin;

        boolean flag = false;
        if (fileList != null) {
            for (File f : fileList) {
                if (f.getName().equals(userName + ".ser")) {
                    flag = true;
                    try {
                        fin = new FileInputStream(f);
                        oin = new ObjectInputStream(fin);

                        this.currentUser = User.deserializeWithHash(oin, pswd);

                        fin.close();
                        oin.close();
                    } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e) {
                        new ContentDialog(e.getMessage()).setVisible(true);
                    } catch (SecurityException ex) {
                        // pswd incorrect
                        throw new PswdIncorrectException();
                    }

                    break;
                }
            }
        }

        if (!flag) {
            // user not found
            throw new UserNotFoundException();
        }
    }

    public void updateSaveFile() {
        try {
            Path saveFilePath = getSavePath().resolve(this.currentUser.getUserName() + ".ser");
            File saveFile = saveFilePath.toFile();

            FileOutputStream fout= new FileOutputStream(saveFile);
            ObjectOutputStream oout= new ObjectOutputStream(fout);

            if (saveFile.exists()) {
                saveFile.delete();
            }

            this.currentUser.serializeWithHash(oout);
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     * Get the current user's user ID
     *
     * @author 秦嘉曜
     * @return the id of the current user. If in guest mode, return -1
     */
    public String getCurrentUser() {
        return this.currentUser.getUserName();
    }

    public String getStageInfo(){
        return currentUser.getSave().getStageInfo();
    }

    public void updateStageInfo(int stageID, int steps, int time){
        this.currentUser.updateSaveStageCompleted(stageID, steps, time);
    }

    /**
     * @author: 秦嘉曜
     * @description: update the save with new MapMatrix
     * @throw:
     */
    public void updateSave(Map map) {
        this.currentUser.updateSave(map);
    }

    public boolean checkLoggedIn() {
        return this.currentUser != null;
    }
}
