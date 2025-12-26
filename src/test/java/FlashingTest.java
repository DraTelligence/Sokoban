import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class FlashingTest {
    private static float alpha = 1.0f; // 初始透明度
    private static boolean fadingOut = true; // 是否正在淡出

    public static void main(String[] args) {
        // 创建 JFrame
        JFrame frame = new JFrame("Flashing Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // 创建 JPanel
        JPanel panel = new JPanel(new BorderLayout());

        // 创建带图片的 JLabel
        ClassLoader classLoader = FlashingTest.class.getClassLoader();
        ImageIcon icon1 = new ImageIcon(classLoader.getResource("icons/Crates/crates1/crate.png"));
        ImageIcon icon2 = new ImageIcon(classLoader.getResource("icons/Crates/crates1/crate_onTarget.png"));
        
        JLabel label = new JLabel(icon1) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawImage(icon1.getImage(), 0, 0, getWidth(), getHeight(), this);
                
                g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha));
                g2d.drawImage(icon2.getImage(), 0, 0, getWidth(), getHeight(), this);
                g2d.dispose();
            }
        };

        // 创建按钮
        JButton button = new JButton("Click Me");
        button.addActionListener((ActionEvent e) -> {
            Timer timer = new Timer(50, (ActionEvent e1) -> {
                if (fadingOut) {
                    alpha -= 0.05f;
                    if (alpha <= 0.0f) {
                        alpha = 0.0f;
                        fadingOut = false;
                        ((Timer) e1.getSource()).stop();
                    }
                } else {
                    alpha += 0.05f;
                    if (alpha >= 1.0f) {
                        alpha = 1.0f;
                        fadingOut = true;
                        ((Timer) e1.getSource()).stop();
                    }
                }
                label.repaint();
            });
            timer.start();
        });

        // 将组件添加到面板
        panel.add(label, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);

        // 将面板添加到框架
        frame.add(panel);

        // 显示框架
        frame.setVisible(true);
    }
}
