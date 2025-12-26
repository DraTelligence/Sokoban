package view.panels;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainMenuPanel extends JPanel {
    private JButton startButton;
    private JButton logInButton;
    private JButton exitButton;
    private JButton settingsButton;
    private JButton[] buttons = new JButton[4];

    public MainMenuPanel() {
        this.setBounds(0, 0, 555, 785);
        this.setOpaque(false);
        this.setLayout(null);

        // add background picture
        InputStream ins = getClass().getResourceAsStream("/icons/Backgrounds/startBackground.png");
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

        JPanel jp=new JPanel();
        jp.setBounds(0,0,555,785);
        jp.setBackground(Color.BLACK);
        jp.setVisible(true);
        this.add(jp);

        // add buttons
        this.startButton = new JButton("start");
        this.logInButton = new JButton("user");
        this.exitButton = new JButton("exit");
        this.settingsButton = new JButton("settings");

        buttons[0] = startButton;
        buttons[1] = logInButton;
        buttons[2] = exitButton;
        buttons[3] = settingsButton;

        this.startButton.setBounds(170, 365, 200, 40);
        this.logInButton.setBounds(170, 425, 200, 40);
        this.settingsButton.setBounds(230, 550, 100, 40);
        this.exitButton.setBounds(170, 480, 200, 40);

        for (var button : buttons) {
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            this.add(button);
        }

        this.setVisible(true);
    }

    public void addMenuListener(ActionListener listener) {
        for (var button : buttons) {
            button.addActionListener(listener);
        }
    }
}