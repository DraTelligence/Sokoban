package view.game;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLayeredPane;

import view.game.buttonPanel.AreaButton;
import view.game.buttonPanel.ButtonDown;
import view.game.buttonPanel.ButtonHint;
import view.game.buttonPanel.ButtonLeft;
import view.game.buttonPanel.ButtonPanelBody;
import view.game.buttonPanel.ButtonRewind;
import view.game.buttonPanel.ButtonRight;
import view.game.buttonPanel.ButtonUp;


public class ButtonPanel extends JLayeredPane{
    final private ArrayList<AreaButton> buttons;

    public ButtonPanel(){
        this.setBounds(0,0,555,785);
        this.setOpaque(false);

        buttons=new ArrayList<>(6);

        buttons.add(new ButtonHint());
        buttons.add(new ButtonLeft());
        buttons.add(new ButtonRight());
        buttons.add(new ButtonUp());
        buttons.add(new ButtonDown());
        buttons.add(new ButtonRewind());

        
        this.add(new ButtonPanelBody());
        for(var bt: buttons){
            this.add(bt,0);
        }

        this.setVisible(true);
    }

    public void handleMouseEvent(MouseEvent e){
        for(var bt:buttons){
            bt.handleMouseEvent(e);
        }
    }

    public ArrayList<AreaButton> getButtons() {
        return buttons;
    }

    public void handleKeyEvent(KeyEvent e){
        for(var bt: buttons){
            bt.handleKeyEvent(e);
        }
    }
}
