package view.panels;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;

import controller.LevelSelectMenuController;

public class LevelSelectMenuPanel extends JPanel {
    static private JButton[] levelButtons=new JButton[5];

    public LevelSelectMenuPanel() {
        this(555, 785); // 默认大小
    }

    public LevelSelectMenuPanel(int width, int height) {
        this.setLayout(null);
        this.setBounds(0, 0, width, height);

        // 使用 getResource 加载背景图片
        ImageIcon loginBackground = new ImageIcon(getClass().getResource("/icons/Backgrounds/levelBackground.png"));
        JLabel lGL = new JLabel(loginBackground);
        lGL.setBounds(0, 0, loginBackground.getIconWidth(), loginBackground.getIconHeight());
        this.add(lGL);

        // 设置面板属性
        this.setOpaque(false);
        this.setSize(loginBackground.getIconWidth(), loginBackground.getIconHeight());

        JButton level1Btn = createButton(new Point(80, 520), 60, 60);
        level1Btn.addActionListener((ActionEvent e)->{
            LevelSelectMenuController.getInstance().select(1);
        });
        JButton level2Btn = createButton(new Point(190, 460), 60, 60);
        level2Btn.addActionListener((ActionEvent e)->{
            LevelSelectMenuController.getInstance().select(2);
        });
        JButton level3Btn = createButton(new Point(280, 410), 60, 60);
        level3Btn.addActionListener((ActionEvent e)->{
            LevelSelectMenuController.getInstance().select(3);
        });
        JButton level4Btn = createButton(new Point(370, 380), 60, 60);
        level4Btn.addActionListener((ActionEvent e)->{
            LevelSelectMenuController.getInstance().select(4);
        });
        JButton level5Btn = createButton(new Point(440, 280), 60, 60);
        level5Btn.addActionListener((ActionEvent e)->{
            LevelSelectMenuController.getInstance().select(5);
        });
        
        levelButtons[0] = level1Btn;
        levelButtons[1] = level2Btn;
        levelButtons[2] = level3Btn;
        levelButtons[3] = level4Btn;
        levelButtons[4] = level5Btn;

        for(var button:levelButtons){
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            this.add(button);
        }
    }

    private JButton createButton(Point location, int width, int height) {
        JButton button = new JButton();
        button.setBounds(location.x, location.y, width, height);
        return button;
    }
}