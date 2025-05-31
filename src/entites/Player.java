package entites;

import utils.Config;

import java.awt.*;
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

    private int jumpY;
    private int y;
    private int velocity = -22;
    private int currState;
    private int dinoImgIdx;
    private int counter = 0;
    private int animationTick;
    private final int animationSpeed;

    private boolean isCollided;
    private boolean isJumpProgress;

    private final BufferedImage[][] images;
    private HashMap<Integer, Point[]> imageDimensions;

    private PlayerListener listener;

    public Player(BufferedImage[][] images) {
        this(images, 12, Player.Y_COORDINATE);
    }

    public Player(BufferedImage[][] images, int animationSpeed, int y) {
        this.images = images;
        this.animationSpeed = animationSpeed;

        currState = Player.IDLE;
        this.y = y;
        this.jumpY = y;
        calcImageDimensions();
    }

    public void draw(Graphics2D g2d, Component c) {
        //System.out.println("STATE:" + currState + ", maxLen:" + bufferedImage[currState].length + ", " + dinoImgIdx);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(images[currState][dinoImgIdx], Player.X_COORDINATE, jumpY, c);

        /* DEBUG: */
        g2d.setColor(Color.red);
        // System.out.println(images[currState][dinoImgIdx]);

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
                if (!isJumpProgress) jumpY = y;
            } else {
                if (currState == Player.DEATH) jumpY = y;
                dinoImgIdx++;
            }
        }
    }

    public void jump() {
        if (currState == Player.JUMPING) {
            isJumpProgress = true;
            jumpY += velocity; // Update the position based on velocity
            velocity += 1; // Apply gravity to velocity

            // Check if the panel has reached or passed the initial Y position
            if (jumpY >= y) {
                counter = 0;
                dinoImgIdx = 0;
                velocity = -22;
                isJumpProgress = false;
                jumpY = y; // Set back to initial position
                currState = Player.RUNNING; // Stop the animation
            }
        }
    }

    private void calcImageDimensions() {
        imageDimensions = new HashMap<>();
        /* Calculating the dimensions of the image of every state */
        for (int state = 0; state < images.length; state++) {
            if (images[state] == null) continue;
            Point[] points = new Point[images[state].length];
            for (int image = 0; image < images[state].length; image++) {
                int imageWidth = images[state][image].getWidth();
                int imageHeight = images[state][image].getHeight();
                points[image] = new Point(imageWidth, imageHeight);
            }
            imageDimensions.putIfAbsent(state, points);
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
        return new Rectangle(Player.X_COORDINATE + 10, jumpY,
                imageDimensions.get(currState)[dinoImgIdx].x - 20,
                imageDimensions.get(currState)[dinoImgIdx].y - 10);
    }

    public int getStateLength(int state) {
        return images[state].length;
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
