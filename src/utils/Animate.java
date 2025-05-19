package utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Animate implements ActionListener {

    public static final int SLIDE_RIGHT = 1;
    public static final int SLIDE_DOWN = 2;
    public static final int HIDE = 3;

    private int currentAnim;
    private final int finalX;
    private final int finalY;
    private final Timer timer;
    private final JComponent widget;
    private boolean isAnimFinished;
    private MenuListener menuListener;

    public Animate(JComponent widget) {
        this.widget = widget;
        this.finalX = widget.getX();
        this.finalY = widget.getY();
        timer = new Timer(2, this);
    }

    public void start(int animation) {
        currentAnim = animation;
        switch (animation) {
            case SLIDE_RIGHT -> widget.setLocation(Config.WIDTH, Config.HEIGHT);
            case SLIDE_DOWN -> widget.setLocation(widget.getX(), Config.HEIGHT);
        }
        timer.start();
        isAnimFinished = false;
    }

    public void stop() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int currentX = widget.getX();
        int currentY = widget.getY();

        try {
            switch (currentAnim) {
                case SLIDE_DOWN -> {
                    if (currentY + widget.getHeight() < 0) {
                        currentY = Config.HEIGHT;
                        isAnimFinished = true;
                    }
                    Thread.sleep(5);
                    widget.setLocation(finalX, currentY - 1);
                }
                case SLIDE_RIGHT -> {
                    if (currentX > (finalX - 40)) {
                        widget.setLocation(currentX - 15, finalY);
                    } else {
                        widget.setLocation(finalX, finalY);
                        ((Timer) e.getSource()).stop();
                        isAnimFinished = true;
                    }
                }
                case HIDE -> {
                    Thread.sleep(2);
                    if (currentX < Config.WIDTH) {
                        widget.setLocation(currentX + 15, finalY);
                    } else {
                        ((Timer) e.getSource()).stop();
                        isAnimFinished = true;
                        if (menuListener != null) menuListener.onMenuHidden();
                    }
                }
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setMenuListener(MenuListener menuListener) {
        this.menuListener = menuListener;
    }

    public boolean isAnimFinished() {
        return isAnimFinished;
    }

    public interface MenuListener {
        void onMenuHidden();
    }
}
