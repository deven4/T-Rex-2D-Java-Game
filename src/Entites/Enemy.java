package Entites;

import Utils.Config;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy {

    private int x;
    private final int y;
    private int enemyImgIdx;
    private final int animationSpeed;
    public boolean isEnemyCrossed;
    private int animationTick;
    private boolean isDinoJumpedAcross;
    private final BufferedImage[] enemyImages;

    public Enemy(BufferedImage[] enemyImages) throws Exception {
        this(enemyImages, Config.WIDTH, Dino.Y_COORDINATE + 10);
    }

    public Enemy(BufferedImage[] enemyImages, int x, int y) throws Exception {
        this.x = x;
        this.y = y;
        this.animationSpeed = 5;
        this.enemyImages = enemyImages;
    }

    public void draw(Graphics2D g2d, Component c) {
        g2d.drawImage(enemyImages[enemyImgIdx], x, y, enemyImages[enemyImgIdx].getWidth(),
                enemyImages[enemyImgIdx].getHeight(), c);

        /* HIT-BOX */
        g2d.setColor(Color.ORANGE);
        System.out.println(enemyImages[enemyImgIdx].getWidth() + ", " + enemyImages[enemyImgIdx].getHeight());
        g2d.drawRect(x, y, enemyImages[enemyImgIdx].getWidth(), enemyImages[enemyImgIdx].getHeight());
    }

    public void animate() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            if (enemyImgIdx >= enemyImages.length - 1) enemyImgIdx = 0;
            else enemyImgIdx++;
        }
    }

    public void move(int speed) {
        if (isEnemyCrossed) return;
        if (x + enemyImages[enemyImgIdx].getWidth() < 0) {
            x = Config.WIDTH;
            isEnemyCrossed = false;
            isDinoJumpedAcross = false;
        } else {
            x = x - speed;
            if (x < Dino.X_COORDINATE && !isDinoJumpedAcross) {
                // GamePanel.score++;
                isDinoJumpedAcross = true;
            }
        }
    }

    public boolean collision(int dinoY, Point imageDims) {
        try {
            Rectangle rectDino = new Rectangle(Dino.X_COORDINATE + 10, dinoY, imageDims.x - 20,
                    imageDims.y - 10);
            Rectangle rectObstacle = new Rectangle(x + 10, y, enemyImages[0].getWidth() - 20,
                    enemyImages[enemyImgIdx].getHeight());
            return rectObstacle.intersects(rectDino);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void resetPosition() {
        this.x = Config.WIDTH;
    }

//    public int getWidth() {
//        return enemyImageArray[0].getWidth() - 20;
//    }
}
