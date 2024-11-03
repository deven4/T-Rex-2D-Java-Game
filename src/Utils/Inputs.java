package Utils;

import java.awt.event.*;

public record Inputs(Listener keyboardListener, Inputs.mouseListener mouseListener)
        implements KeyListener, MouseListener, MouseMotionListener {

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (keyboardListener == null) return;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                //keyboardListener.onUpPressed(-unitSize);
            }
            case KeyEvent.VK_SPACE -> keyboardListener.onSpaceBarPressed();
            case KeyEvent.VK_ESCAPE -> keyboardListener.onEscapeKeyPressed();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseListener.onMouseClicked(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
       // mouseListener.onMouseDragged(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseListener.onMouseHover(e.getX(), e.getY());
    }

    public interface Listener {

        void onUpPressed(int value);

        void onSpaceBarPressed();

        void onEscapeKeyPressed();
    }

    public interface mouseListener {
        void onMouseHover(int x, int y);

        void onMouseClicked(int x, int y);
    }
}
