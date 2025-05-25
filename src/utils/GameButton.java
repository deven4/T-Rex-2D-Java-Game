package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GameButton extends JPanel {
    private final GameFont gameFont = GameFont.getInstance();
    private Font font = gameFont.getSuperDream();
    private JButton button;
    private boolean isButton;

    public GameButton(String text) {
        initialize(text, true);
    }

    public GameButton(String text, Font font) {
        this.font = font;
        initialize(text, true);
    }

    public GameButton(String text, boolean isButton) {
        initialize(text, isButton);
    }

    public GameButton(String text, boolean isButton, Font font) {
        this.font = font;
        initialize(text, isButton);
    }

    private void initialize(String text, boolean isButton) {
        this.isButton = isButton;
        button = new JButton(text);
        button.setFont(font);
        button.setOpaque(true);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setForeground(Config.SECONDARY_COLOR);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(Color.CYAN);

        onHover();
        setOpaque(false);
        setLayout(new GridBagLayout());

        Color[] color = new Color[3];
        color[0] = Color.ORANGE;
        color[1] = Color.LIGHT_GRAY;
        color[2] = Color.CYAN;
        setBackground(color[new Random().nextInt(3)]);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 10, 0);  // Add bottom padding to the button
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(button, gbc);
    }

    private void onHover() {
        GameSound gameSound = GameSound.getInstance();
        if (isButton) {
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            // Add a hover effect
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setContentAreaFilled(true);
                    button.setForeground(Config.PRIMARY_COLOR);
                    button.setBackground(Config.SECONDARY_COLOR);
                    gameSound.play(GameSound.TRACK.BUTTON_HOVER);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setContentAreaFilled(false);
                    button.setForeground(Config.SECONDARY_COLOR);
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

        paintChildren(g);
        g2.dispose();
    }

    public void setOnClickListener(ActionListener clickListener) {
        button.addActionListener(clickListener);
    }
}
