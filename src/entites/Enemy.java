package entites;

import animations.Animation;
import animations.AnimationManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * IDLE animation should be there in all enemies
 **/
public class Enemy {

    private EnemyState currentState = EnemyState.IDLE;

    private int x;
    private final int y;
    private int currFrame;
    private boolean isAlive;
    private boolean isDying;
    private int animationTick;
    private final EnemyType type;
    private final int animationSpeed;
    private final AnimationManager animManager;

    public Enemy(EnemyType type, int x) {
        this.x = x;
        this.type = type;
        this.isAlive = true;
        animationSpeed = 5;
        animManager = new AnimationManager(type.name());

        switch (type) {
            case CACTUS -> y = Dino.Y_COORDINATE + 10;
            case SKELETON_BOMB -> y = Dino.Y_COORDINATE + 30;
            default -> y = Dino.Y_COORDINATE;
        }
    }

    public void draw(Graphics2D g2d, Component c) {
        BufferedImage frame = animManager.getAnimation(currentState).getFrames()[currFrame];
        g2d.drawImage(frame, x, y, frame.getWidth(), frame.getHeight(), c);

        /* HIT-BOX */
        //System.out.println(enemyImages[enemyImgIdx].getWidth() + ", " + enemyImages[enemyImgIdx].getHeight());
        g2d.setColor(Color.ORANGE);
        g2d.drawRect(x, y, frame.getWidth() - 20, frame.getHeight());
    }

    public void animate() {
        Animation anim = animManager.getAnimation(currentState);
        if (anim == null) return;

        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            currFrame++;

            if (currFrame >= anim.getFrames().length - 1) {
                if (anim.isLooping()) {
                    currFrame = 0;
                } else {
                    currFrame = anim.getFrames().length - 1;  // stop at last frame
                    if (currentState == EnemyState.EXPLODE) isAlive = false;
                }
            }
        }
    }

    public void update(int speed) {
        x -= speed;
    }

    public boolean hasEnemyMovedOutOffScreen() {
        return x + animManager.getAnimation(currentState).getFrames()[currFrame].getWidth() < 0;
    }

    public void die() {
        if (!isAlive || isDying) return;

        setState(EnemyState.EXPLODE);
        isDying = true;
    }

    public void setState(EnemyState newState) {
        if (currentState != newState) {
            currentState = newState;
            currFrame = 0;
            animationTick = 0;
        }
    }

    public Rectangle getBounds() {
        BufferedImage frame = animManager.getAnimation(currentState).getFrames()[currFrame];
        return new Rectangle(x, y, frame.getWidth() - 20, frame.getHeight());
    }

    public int getX() {
        return x;
    }

    public EnemyType getType() {
        return type;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isDying() {
        return isDying;
    }
}
