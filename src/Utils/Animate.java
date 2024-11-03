package Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Animate implements ActionListener {

    public static final int SLIDE_RIGHT = 1;
    public static final int SLIDE_DOWN = 2;
    public static final int HIDE = 0;
    private final int finalX;
    private final int finalY;
    private final Timer timer;
    private final JComponent widget;
    private final int animation;
    private boolean isHide;

    public Animate(JComponent widget, int animationToPlay) {
        this.widget = widget;
        this.animation = animationToPlay;
        this.finalX = widget.getX();
        this.finalY = widget.getY();
        timer = new Timer(1, this);
    }

    public void start() {
        switch (animation) {
            case SLIDE_RIGHT -> widget.setLocation(GameConfig.WIDTH, GameConfig.HEIGHT);
            case SLIDE_DOWN -> widget.setLocation(widget.getX(), GameConfig.HEIGHT);
        }
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void hide() {
        isHide = true;
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int currentX = widget.getX();
        int currentY = widget.getY();
//        System.out.println(currentY + "," + widget.getBounds().y);
        if (isHide) {
            if (currentX < GameConfig.WIDTH) {
                widget.setLocation(currentX + 15, finalY);
            } else {
                ((Timer) e.getSource()).stop();
                isHide = false;
            }
            return;
        }

        switch (animation) {
            case SLIDE_DOWN -> {
                System.out.println(currentY + "," + finalY);
                for (int i = 0; i < widget.getComponents().length; i++) {
                    JLabel lbl = (JLabel) widget.getComponents()[i];
                    currentY = lbl.getY() - 1;
                    if (currentY + lbl.getHeight() < 0) {
                        // If label is out of view at the top, reset it to the bottom
                        currentY = GameConfig.HEIGHT;
                    }
                    lbl.setLocation(lbl.getX(), currentY);
                }
            }
            case SLIDE_RIGHT -> {
                if (currentX > (finalX - 40)) {
                    widget.setLocation(currentX - 15, finalY);
                } else {
                    widget.setLocation(finalX, finalY);
                    ((Timer) e.getSource()).stop();
                }
            }
        }
    }
}
