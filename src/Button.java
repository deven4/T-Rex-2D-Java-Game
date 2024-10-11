import Utils.GameSound;
import Utils.Inputs;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;

public class Button extends JComponent implements Inputs.mouseListener {

    private String text;
    private Point coordinates;
    private final Font font;
    private final FontMetrics fontMetrics;
    private onClickListener onClickListener;

    private int animationX;
    private int animationSpeed;
    private boolean isHidden;
    private boolean isHovered;
    private boolean isAnimOver;
    private boolean isPlainText;
    private boolean isVisible;
    private final Font hoveredFont;
    private final Clip btnHoverClip;
    private boolean isMusicPlaying;

    private int counter;

    public Button(GamePanel gamePanel, String text, Point coordinates, Font font) {
        this.text = text;
        this.font = font;
        this.fontMetrics = getFontMetrics(font);
        this.coordinates = coordinates;
        Inputs input = new Inputs(null, this);
        gamePanel.addMouseListener(input);
        gamePanel.addMouseMotionListener(input);
        this.animationX = Game.WIDTH;
        this.animationSpeed = 8;
        this.hoveredFont = font.deriveFont(50f);
        this.btnHoverClip = GameSound.getInstance().getBtnHoverClipSound();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public void hide(boolean isHidden) {
        this.isHidden = isHidden;
        if(!isHidden) {
            isAnimOver = false;
            isVisible = true;
        }
    }

    public void setAnimationSpeed(int animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    public void setListener(onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private int getAscend() {
        return fontMetrics.getAscent();
    }

    public int getWidth() {
        return fontMetrics.stringWidth(text);
    }

    public void draw(Graphics graphics) {
        graphics.setFont(font);
        if (!isAnimOver) {
            if (animationX <= coordinates.x - 40) {
                animationX = coordinates.x;
                isAnimOver = true;
            } else animationX -= animationSpeed;
        } else {
            if (isHovered) {
                if (!isMusicPlaying) {
                    btnHoverClip.setFramePosition(0);
                    btnHoverClip.start();
                    isMusicPlaying = true;
                }
                graphics.setColor(Color.RED);
                graphics.setFont(hoveredFont);
                counter += 1;
            } else {
                if (isMusicPlaying) {
                    btnHoverClip.stop();
                    btnHoverClip.setFramePosition(0);
                    isMusicPlaying = false;
                }
                graphics.setColor(Color.BLACK);
                graphics.setFont(font);
            }
        }
        if (isHidden) {
            if (animationX <= Game.WIDTH)
                animationX += 8;
            else isVisible = false;
        } else {
            if (isAnimOver) animationX = coordinates.x;
        }
        if(isVisible)
            graphics.drawString(text, animationX, coordinates.y + getAscend());
    }

    public void isPlainText(boolean isPlainText) {
        this.isPlainText = isPlainText;
    }

    public void drawRect(Graphics graphics) {
        graphics.drawRect(coordinates.x, coordinates.y,
                fontMetrics.stringWidth(text), fontMetrics.getAscent());
    }

    private boolean isClickInsideRect(int mouseX, int mouseY) {
        Rectangle rectangle = new Rectangle(coordinates.x, coordinates.y,
                fontMetrics.stringWidth(text), fontMetrics.getAscent());
        return rectangle.contains(mouseX, mouseY);
    }

    @Override
    public void onMouseHover(int x, int y) {
        if(isPlainText || isHidden) return;
        isHovered = isClickInsideRect(x, y);
    }

    @Override
    public void onMouseClicked(int x, int y) {
        if(isPlainText || isHidden) return;
        if (isClickInsideRect(x, y)) {
            if (onClickListener != null)
                onClickListener.onClick();
        }
    }

    public interface onClickListener {
        void onClick();
    }
}
