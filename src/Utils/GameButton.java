package Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameButton extends JButton {

    private final Color hoverColor = Color.BLACK;
    private boolean onHover;
    private Color backgroundColor = new Color(70, 130, 180);

    public GameButton(String text) {
        super(text);
        initialise(true);
    }

    public GameButton(String text, boolean isButton) {
        super(text);
        initialise(isButton);
    }

    private void initialise(boolean onHover) {
        this.onHover = onHover;
        GameFont gameFont = GameFont.getInstance();
        setFont(gameFont.getSuperDream());
        setContentAreaFilled(false); // removes default button background
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.BLACK);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (onHover) {
            // Add a hover effect
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    backgroundColor = hoverColor;
                    setContentAreaFilled(true);
                    setForeground(Color.red);
                    setBackground(backgroundColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    backgroundColor = new Color(70, 130, 180);
                    setContentAreaFilled(false);
                    setForeground(Color.BLACK);
                    //setFont(font.deriveFont(GameFont.getSize()));
                }
            });
        }
    }

    public void addMouseListener(boolean onHover) {
        this.onHover = onHover;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set background color and paint rounded rectangle
        g2.setColor(backgroundColor);
//        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
    }

    public boolean isOnHover() {
        return onHover;
    }

    public void setOnHover(boolean onHover) {
        this.onHover = onHover;
    }
}
