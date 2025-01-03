import Utils.*;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Inputs.Listener {

    private final int[] forestX;
    private int dinoImgIdx;
    private int enemyImgIdx;
    private int currState;
    private int animationTick;
    private final Dino dino;
    private int counter = 0;
    private int animationSpeed;
    private int dinoY;
    private int score;
    private int enemyX;
    private int forestSpeed = 1;
    private boolean isPlayPressed;
    private boolean isGameStarted;
    private final Clip introClip;
    private final BufferedImage forestImg;
    private boolean isShowObstacles;
    boolean isEnemyCrossed;
    private int velocity = -22;
    private final GameLabel lblStart;
    private final Enemy enemy;
    private final GameMenuPanel gameMenuPanel;
    private int testCount = 1;

    public GamePanel() throws Exception {
        super();
        dino = new Dino();
        forestX = new int[2];
        enemy = new Enemy();
        forestImg = new ForestBG().getBufferedImage()[1];
        forestX[1] = forestX[0] + forestImg.getWidth();
        animationSpeed = 12;
        enemyX = GameConfig.WIDTH;
        currState = Dino.IDLE;
        dinoY = Dino.Y_COORDINATE;
        introClip = GameSound.getInstance().getIntroClip();
        lblStart = new GameLabel("Press space bar to start", GameConfig.WIDTH / 2 - 150, 150);
        gameMenuPanel = new GameMenuPanel(this);

        gameMenuPanel.showMenu(GameMenuPanel.Menu.MAIN);
        addKeyListener(new Inputs(this, null));

        requestFocus();
        setLayout(null);
        add(gameMenuPanel);
        setFocusable(true);
        setPreferredSize(new Dimension(GameConfig.WIDTH, GameConfig.HEIGHT));
    }

    public void startGame() {
        requestFocus();
        setFocusable(true);
        lblStart.show(true);
        isGameStarted = true;
        isPlayPressed = true;
        currState = Dino.WALKING;
        introClip.stop();
    }

    private void endGame() {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            move();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /* Draw Forest */
        g.drawImage(forestImg, forestX[0], 0, forestImg.getWidth(), GameConfig.HEIGHT, this);
        g.drawImage(forestImg, forestX[1], 0, forestImg.getWidth(), GameConfig.HEIGHT, this);

        /* Draw Dino */
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(dino.getImages()[currState][dinoImgIdx], Dino.X_COORDINATE, dinoY, this);

        /* Draw Cactus */
        g2d.drawImage(enemy.getCactusImage()[enemyImgIdx], enemyX, Enemy.Y,
                enemy.getCactusImage()[enemyImgIdx].getWidth(),
                enemy.getCactusImage()[enemyImgIdx].getHeight(), null);
        lblStart.draw(g);

        /* Draw Score Label */
        g.setFont(GameFont.getInstance().getSuperDream());
        g.drawString(STR."\{score}", 50,50);
        if(enemyX < Dino.X_COORDINATE && !isEnemyCrossed) {
            isEnemyCrossed = true;
            score++;
        }

        /* DEBUG: */
//        if (rectDino != null) {
//            g2d.setColor(Color.red);
//            g.drawRect(rectDino.x, rectDino.y, rectDino.width, rectDino.height);
//        }
        g2d.setColor(Color.ORANGE);
        g.drawRect(enemyX + 10, Enemy.Y, enemy.getWidth(), enemy.getCactusImage()[enemyImgIdx].getHeight());
    }

    private void move() throws Exception {
        if (currState == Dino.JUMPING) animationSpeed = 25;
        else animationSpeed = 12;
        if (!gameMenuPanel.isMenuVisible()) {
            if (isGameStarted) {
                if (checkDinoCollision()) {
                    currState = Dino.DEATH;
                } else {
                    moveBackground();
                }
                animate();
            }
        } else if (currState == Dino.IDLE) animate();
    }

    private boolean checkDinoCollision() throws Exception {
        Rectangle rectDino = new Rectangle(Dino.X_COORDINATE + 10, dinoY,
                dino.getImageHeightWidth(currState).x - 20, dino.getImageHeightWidth(currState).y - 10);
        Rectangle rectObstacle = new Rectangle(enemyX + 10, Enemy.Y, enemy.getWidth(),
                enemy.getCactusImage()[enemyImgIdx].getHeight());
        return rectObstacle.intersects(rectDino);
    }

    private void moveBackground() {
        forestX[0] = forestX[0] - forestSpeed;
        forestX[1] = forestX[1] - forestSpeed;

        if (forestX[0] + forestImg.getWidth() <= 0) {
            forestX[0] = forestX[1] + forestImg.getWidth();
        }
        if (forestX[1] + forestImg.getWidth() <= 0) {
            forestX[1] = forestX[0] + forestImg.getWidth();
        }

        /* Move Obstacle */
        if (isShowObstacles) {
            if (enemyX + enemy.getCactusImage()[enemyImgIdx].getWidth() < 0) {
                isEnemyCrossed = false;
                enemyX = GameConfig.WIDTH;
            }
            enemyX = enemyX - forestSpeed;
        }
    }

    private void animate() {
        counter += 1;
        animationTick++;

        if (currState == Dino.JUMPING && counter % 3 == 0) {
            // Update the position based on velocity
            dinoY += velocity;
            velocity += 1; // Apply gravity to velocity

            // Check if the panel has reached or passed the initial Y position
            if (dinoY >= Dino.Y_COORDINATE) {
                dinoY = Dino.Y_COORDINATE; // Set back to initial position
                currState = Dino.RUNNING; // Stop the animation
            }
        }
        if (animationTick >= animationSpeed) {
            animationTick = 0;

            // Animate Enemy
            if (enemyImgIdx >= enemy.getCactusImage().length - 1) enemyImgIdx = 0;
            else enemyImgIdx++;

            // Animate Dino
            if (dinoImgIdx >= dino.getStateLength(currState) - 1) {
                dinoImgIdx = 0;
                if (currState == Dino.JUMPING) {
                    currState = Dino.RUNNING;
                } else if (currState == Dino.DEATH) {
                    isGameStarted = false;
                    dinoImgIdx = dino.getStateLength(currState) - 1;
                }
                dinoY = Dino.Y_COORDINATE;
            } else dinoImgIdx++;
        }
    }

    @Override
    public void onUpPressed() {
        if (currState == Dino.DEATH) currState = Dino.WALKING;
        else currState = Dino.DEATH;
    }

    @Override
    public void onSpaceBarPressed() {
        if (!isGameStarted) return;

        if (isPlayPressed) {
            forestSpeed = 2;
            isPlayPressed = false;
            isShowObstacles = true;
            currState = Dino.RUNNING;
            lblStart.show(false);
        } else if (currState != Dino.JUMPING) {
            counter = 0;
            dinoImgIdx = 0;
            velocity = -22;
            currState = Dino.JUMPING;
        }
    }

    @Override
    public void onEscapeKeyPressed() {
        if (!Game.isGamePaused && isGameStarted) {
            Game.isGamePaused = true;
            gameMenuPanel.showMenu(GameMenuPanel.Menu.PAUSE);
        }
    }
}
