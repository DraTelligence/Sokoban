package view.game.buttonPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

@SuppressWarnings("FieldMayBeFinal")
public class ButtonDown extends ButtonDirection {

    public ButtonDown() {
        this.setBounds(0,0,555,900);
        this.setOpaque(false);

        Area buttonArea = new Area(new RoundRectangle2D.Double(210,678,135,50,10,10));
        this.setButtonArea(buttonArea);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(this.getButtonColor());
        g2d.fill(this.getButtonArea());

        g2d.dispose();
    }

    @Override
    public String getCommand() {
        return "DOWN";
    }
}