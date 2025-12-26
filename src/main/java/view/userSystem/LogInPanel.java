package view.userSystem;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.AppController;
import controller.UserSystemController;
import model.exceptions.PswdIncorrectException;
import model.exceptions.UserAlreadyExistsException;
import model.exceptions.UserNotFoundException;
import view.app.ContentDialog;

public class LogInPanel extends JLayeredPane {
    private JTextField username;
    private JTextField password;
    final private JButton logInBtn;
    final private JButton signUpBtn;

    @SuppressWarnings({ "unused" })
    public LogInPanel() {
        this.setBounds(0, 0, 555, 785);
        this.setVisible(true);

        // set background
        InputStream ins = getClass().getResourceAsStream("/icons/Backgrounds/loginBackground.png");
        if (ins != null) {
            try {
                ImageIcon loginBackground = new ImageIcon(ImageIO.read(ins));
                loginBackground.setImage(loginBackground.getImage().getScaledInstance(555, 785, Image.SCALE_DEFAULT));
                JLabel BgImage = new JLabel(loginBackground);
                BgImage.setBounds(0, 0, 555, 785);
                BgImage.setVisible(true);
                this.add(BgImage);
                this.setComponentZOrder(BgImage, this.getComponentCount() - 1);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    ins.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            System.err.println("Background image not found.");
        }

        // set a panel to contain other comps
        JPanel userSystemPanel = new JPanel();
        userSystemPanel.setBounds(0, 0, 555, 785);
        userSystemPanel.setOpaque(false);
        userSystemPanel.setLayout(null);

        this.add(userSystemPanel);
        this.moveToFront(userSystemPanel);

        // add other comps
        this.username = createJTextField(new Point(230, 255), 160, 40);
        this.password = createJTextField(new Point(230, 335), 160, 40);

        signUpBtn = createJButton(new Point(100, 435), 140, 40);
        signUpBtn.setContentAreaFilled(false);
        signUpBtn.setBorderPainted(false);
        logInBtn = createJButton(new Point(285, 435), 140, 40);
        logInBtn.setContentAreaFilled(false);
        logInBtn.setBorderPainted(false);

        userSystemPanel.add(username);
        userSystemPanel.add(password);
        userSystemPanel.add(logInBtn);
        userSystemPanel.add(signUpBtn);

        signUpBtn.addActionListener((ActionEvent e) -> {
            try {
                UserSystemController.getInstance().signUp(username.getText(), password.getText());
                new ContentDialog("Sign up successfully!").setVisible(true);
            } catch (UserAlreadyExistsException ex) {
                JDialog dialog = new ContentDialog("user already exists!");
            } catch (NullPointerException ex) {
                JDialog dialog = new ContentDialog("username or password cannot be empty!");
            }
        });

        logInBtn.addActionListener(e -> {
            try {
                UserSystemController.getInstance().logIn(username.getText(), password.getText());
                new ContentDialog("Log in successfully!").setVisible(true);
                AppController.getInstance().switchToMainMenu();
            } catch (UserNotFoundException ex) {
                new ContentDialog("No such user!").setVisible(true);
            } catch (PswdIncorrectException ex) {
                new ContentDialog("Password incorrect!").setVisible(true);
            }
        });
    }

    private static JTextField createJTextField(Point location, int width, int height) {
        JTextField jTextField = new JTextField();
        jTextField.setSize(width, height);
        jTextField.setLocation(location);
        return jTextField;
    }

    private static JButton createJButton(Point location, int width, int height) {
        JButton button = new JButton();
        button.setSize(width, height);
        button.setLocation(location);
        return button;
    }
}
