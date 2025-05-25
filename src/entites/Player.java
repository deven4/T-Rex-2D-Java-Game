package entites;

import utils.Config;
import utils.InputManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Player {

    public static final int WALKING = 0;
    public static final int RUNNING = 1;
    public static final int JUMPING = 2;
    public static final int IDLE = 3;
    public static final int DEATH = 4;
    public static final int NUM_STATES = 5;
    public static final int X_COORDINATE = Config.WIDTH / 4;
    public static final int Y_COORDINATE = 360;

    private int dinoY;
    private int velocity;
    private int currState;
    private int dinoImgIdx;
    private int counter = 0;
    private int animationTick;
    private final int animationSpeed;

    private boolean isCollided;
    private boolean isJumpProgress;

    private final BufferedImage[][] bufferedImage;
    private final HashMap<Integer, Point[]> imageDimensions;

    private PlayerListener listener;

    public Player(BufferedImage[][] bufferedImage) {
        velocity = -22;
        animationSpeed = 12;
        currState = Player.IDLE;
        dinoY = Player.Y_COORDINATE;
        imageDimensions = new HashMap<>();
        this.bufferedImage = bufferedImage;

        calcImageDimensions();
    }

    public void draw(Graphics2D g2d, Component c) {
        //System.out.println("STATE:" + currState + ", maxLen:" + bufferedImage[currState].length + ", " + dinoImgIdx);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(bufferedImage[currState][dinoImgIdx], Player.X_COORDINATE, dinoY, c);

        /* DEBUG: */
        /* if (rectDino != null) {
            g2d.setColor(Color.red);
            g.drawRect(rectDino.x, rectDino.y, rectDino.width, rectDino.height);
        }*/
    }

    public void update() {
        //System.out.println(currState + ":" + dinoImgIdx);
        counter += 1;
        animationTick++;

        if (counter % 3 == 0) jump();
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            if (dinoImgIdx >= getStateLength(currState) - 1) {
                dinoImgIdx = 0;
                if (currState == Player.DEATH) {
                    dinoImgIdx = getStateLength(currState) - 1;
                    if (listener != null && !isCollided) {
                        isCollided = true;
                        listener.onDeath();
                    }
                }
                if (!isJumpProgress) dinoY = Player.Y_COORDINATE;
            } else {
                if (currState == Player.DEATH) dinoY = Player.Y_COORDINATE;
                dinoImgIdx++;
            }
        }
    }

    public void jump() {
        if (currState == Player.JUMPING) {
            isJumpProgress = true;
            dinoY += velocity; // Update the position based on velocity
            velocity += 1; // Apply gravity to velocity

            // Check if the panel has reached or passed the initial Y position
            if (dinoY >= Player.Y_COORDINATE) {
                counter = 0;
                dinoImgIdx = 0;
                velocity = -22;
                isJumpProgress = false;
                dinoY = Player.Y_COORDINATE; // Set back to initial position
                currState = Player.RUNNING; // Stop the animation
            }
        }
    }

    private void calcImageDimensions() {
        /* Calculating the dimensions of the image of every state */
        for (int state = 0; state < bufferedImage.length; state++) {
            Point[] points = new Point[bufferedImage[state].length];
            for (int image = 0; image < bufferedImage[state].length; image++) {
                int imageWidth = bufferedImage[state][image].getWidth();
                int imageHeight = bufferedImage[state][image].getHeight();
                points[image] = new Point(imageWidth, imageHeight);
            }
            imageDimensions.computeIfAbsent(state, _ -> points);
        }
        /*
        for (Map.Entry<Integer, Point[]> entry : imageDimensions.entrySet()) {
            Integer key = entry.getKey();
            Point[] points = entry.getValue();

            System.out.print("Key: " + key + " -> Values: ");
            for (Point p : points) {
                System.out.print("(" + p.x + ", " + p.y + ") ");
            }
            System.out.println();
        }
         */
    }

    public void setState(int currState) {
        dinoImgIdx = 0;
        this.currState = currState;
    }

    public void setListener(PlayerListener listener) {
        this.listener = listener;
    }

    public Rectangle getBounds() {
        return new Rectangle(Player.X_COORDINATE + 10, dinoY,
                imageDimensions.get(currState)[dinoImgIdx].x - 20,
                imageDimensions.get(currState)[dinoImgIdx].y - 10);
    }

    public int getStateLength(int state) {
        return bufferedImage[state].length;
    }

    public int getY() {
        return dinoY;
    }

    public int getVelocity() {
        return velocity;
    }

    public void resetPos() {
        velocity = -22;
        isCollided = false;
        setState(Player.IDLE);
    }
}
