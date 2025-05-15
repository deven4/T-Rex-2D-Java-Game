package Entites;

import Utils.Config;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy {

    private int x;
    private int idx;
    private final int y;
    private String tag;
    private final int animationSpeed;
    public boolean isEnemyCrossed;
    private int animationTick;
    private boolean isDinoJumpedAcross;
    private final BufferedImage[] enemyImages;

    public Enemy(BufferedImage[] enemyImages) throws Exception {
        this(enemyImages, Config.WIDTH, Dino.Y_COORDINATE + 10);
    }

    public Enemy(BufferedImage[] enemyImages, int x) throws Exception {
        this(enemyImages, x, Dino.Y_COORDINATE + 10);
    }

    public Enemy(BufferedImage[] enemyImages, int x, int y) throws Exception {
        this.x = x;
        this.y = y;
        this.tag = "E-" + new Random().nextInt(999);
        this.animationSpeed = 5;
        this.enemyImages = enemyImages;
    }

    public void draw(Graphics2D g2d, Component c) {
        g2d.drawImage(enemyImages[idx], x, y, enemyImages[idx].getWidth(), enemyImages[idx].getHeight(), c);

        /* HIT-BOX */
        g2d.setColor(Color.ORANGE);
        //System.out.println(enemyImages[enemyImgIdx].getWidth() + ", " + enemyImages[enemyImgIdx].getHeight());
        g2d.drawRect(x, y, enemyImages[idx].getWidth(), enemyImages[idx].getHeight());
    }

    public void animate() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            if (idx >= enemyImages.length - 1) idx = 0;
            else idx++;
        }
    }

    public void update(int speed) {
        x -= speed;
    }

    public boolean collision(int dinoY, Point imageDims) {
        try {
            Rectangle rectDino = new Rectangle(Dino.X_COORDINATE + 10, dinoY, imageDims.x - 20, imageDims.y - 10);
            Rectangle rectObstacle = new Rectangle(x + 10, y, enemyImages[0].getWidth() - 20, enemyImages[idx].getHeight());
            return rectObstacle.intersects(rectDino);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasEnemyMovedOutOffScreen() {
        return x + enemyImages[idx].getWidth() < 0;
    }

    public void resetPosition() {
        this.x = Config.WIDTH;
    }

    public int getX() {
        return x;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

//    public int getWidth() {
//        return enemyImageArray[0].getWidth() - 20;
//    }
}
