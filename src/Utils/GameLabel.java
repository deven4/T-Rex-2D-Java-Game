package Utils;

import java.awt.*;

public class GameLabel {

    private final int locX;
    private final int locY;
    private int counter;
    private String text;
    private boolean isVisible;
    private final GameFont gameFont;

    public GameLabel(String text, int locX, int locY) {
        this.text = text;
        this.locX = locX;
        this.locY = locY;
        this.gameFont = new GameFont();
    }

    public void draw(Graphics graphics) {
        if (!isVisible) return;
        if (counter >= 30 && counter <= 70) {
            graphics.setFont(gameFont.getSuperDream().deriveFont(30f));
            graphics.drawString(text, locX, locY);
        }
        if (counter >= 70) counter = 0;
        counter += 1;
    }

    public void show(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setText(String text) {
        this.text = text;
    }

}
