package view.userSystem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import controller.AppController;
import controller.UserSystemController;

//import controller.MainMenuController;

@SuppressWarnings("unused")
public class UserProfilePanel extends JPanel {
    private JButton out;
    private JTextField username;

    private JPanel scrollPanel;
    private JTextArea textArea;
    private JPanel panel1;
    private JScrollPane scrollPane;
    private JLabel name;

    public UserProfilePanel() {
        ImageIcon loginBackground = new ImageIcon(getClass().getResource("/icons/Backgrounds/userProfileBackground.png"));
        Image img = loginBackground.getImage();
        Image newImg = img.getScaledInstance(555, 785, Image.SCALE_SMOOTH);
        loginBackground = new ImageIcon(newImg);
        JLabel lGL = new JLabel(loginBackground);
        lGL.setBounds(0, 0, loginBackground.getIconWidth(), loginBackground.getIconHeight());
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        setSize(loginBackground.getIconWidth(), loginBackground.getIconHeight());

        this.setSize(555, 785);
        this.setLayout(null);

        JButton out = new JButton("out");
        out.setSize(130, 60);
        out.setLocation(new Point(210, 663));
        out.setOpaque(false);

        name = new JLabel();
        name.setText(UserSystemController.getInstance().getUserName());
        name.setLocation(230, 170);
        name.setSize(100, 50);
        name.setFont(new Font("", Font.PLAIN, 30));
        name.setVisible(true);

        panel1 = new JPanel();
        panel1.setLocation(75, 320);
        panel1.setSize(400, 300);
        panel1.setOpaque(false);
        panel1.setVisible(true);

        scrollPanel = new JPanel();
        scrollPanel.setOpaque(false);

        textArea = new JTextArea();
        textArea.setOpaque(false);
        textArea.setFont(new Font("", Font.PLAIN, 20));
        for (int i = 0; i < 50; i++) {
            textArea.append("       " + (i + 1) + "              " + "通关" + "             " + "11" + "          " + "  3min \n");
        }

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        scrollPane.setVisible(true);

        panel1.add(scrollPane);
        this.add(panel1);

        this.add(name);
        this.add(lGL);
        this.add(out);
        this.setVisible(true);

        out.addActionListener((ActionEvent e) -> {
            AppController.getInstance().switchToMainMenu();
        });
    }

    public void update(){
        this.name.setText(UserSystemController.getInstance().getUserName());

        this.textArea.setText(UserSystemController.getInstance().getStageInfo());
    }
}
