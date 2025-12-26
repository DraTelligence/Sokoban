package model.user;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import model.game.Map;

//储存用户信息，提供从读取存档文件创建user
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Serial
    final private String userName;

    @Serial
    final private String pswd;

    @Serial
    final private Save save;

    public User(String userName, String pswd) {
        this.userName = userName;
        this.pswd = pswd;
        this.save = new Save();
    }

    // Serialize the object and include a hash of the data using a password
    public void serializeWithHash(ObjectOutputStream oos)
            throws NoSuchAlgorithmException, IOException {
        // Serialize the object
        oos.writeObject(this);

        // Compute the hash of the serialized object with the password
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream tempOos = new ObjectOutputStream(baos);
        tempOos.writeObject(this);
        byte[] objectBytes = baos.toByteArray();
        byte[] hash = computeHash(objectBytes, this.pswd);

        // Write the hash to the file
        oos.writeObject(hash);
    }

    // Deserialize the object and verify the hash using a password
    public static User deserializeWithHash(ObjectInputStream ois, String password)
            throws IOException, ClassNotFoundException, NoSuchAlgorithmException, SecurityException {
        // Deserialize the object
        User user = (User) ois.readObject();

        // Read the stored hash
        byte[] storedHash = (byte[]) ois.readObject();

        // Compute the hash of the deserialized object with the password
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream tempOos = new ObjectOutputStream(baos);
        tempOos.writeObject(user);
        byte[] objectBytes = baos.toByteArray();
        byte[] computedHash = computeHash(objectBytes, password);

        // Verify the hash
        if (!Arrays.equals(storedHash, computedHash)) {
            throw new SecurityException("Data integrity check failed. The file may have been tampered with.");
        }

        return user;
    }

    // Compute the hash of the given data using a password
    private static byte[] computeHash(byte[] data, String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(password.getBytes());
        return digest.digest(data);
    }
    
    protected String getPswd() {
        return pswd;
    }

    protected String getUserName(){
        return userName;
    }

    void updateSave(Map map) {
        this.save.setCurrMap(map);
    }

    void updateSaveStageCompleted(int stageID, int minSteps, int minTime) {
        this.save.updateStageInfo(stageID, minSteps, minTime);
    }

    Save getSave() {
        return save;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User{");
        sb.append("userName=").append(userName);
        sb.append(", pswd=").append(pswd);
        return sb.toString();
    }
}
