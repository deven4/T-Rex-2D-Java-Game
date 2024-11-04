package Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameButton extends JButton {


    public GameButton(String text) {
        super(text);
        initialise(true);
    }

    public GameButton(String text, boolean isButton) {
        super(text);
        initialise(isButton);
    }

    private void initialise(boolean onHover) {
        GameFont gameFont = GameFont.getInstance();
        setFont(gameFont.getSuperDream());
        setContentAreaFilled(false); // removes default button background
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(GameConfig.SECONDARY_COLOR);

        if (onHover) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            // Add a hover effect
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setContentAreaFilled(true);
                    setForeground(GameConfig.PRIMARY_COLOR);
                    setBackground(GameConfig.SECONDARY_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setContentAreaFilled(false);
                    setForeground(GameConfig.SECONDARY_COLOR);
                    //setFont(font.deriveFont(GameFont.getSize()));
                }
            });
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set background color and paint rounded rectangle
        g2.setColor(GameConfig.PRIMARY_COLOR);
//        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
    }

}
