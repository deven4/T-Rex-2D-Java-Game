package core;

import entites.Dino;
import entites.Enemy;
import entites.EnemyState;
import loaders.Assets;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static utils.EnemyUtil.getRandomSpacing;

public class GamePanel extends JPanel implements Inputs.Listener {

    public static int score;

    private int forestSpeed;
    private final int[] forestX;
    private boolean isPlayPressed;
    private final BufferedImage levelBackground;

    private final Dino dino;
    private List<Enemy> enemies;
    private Enemy enemyCausedDeath;
    private final GameSound gameSound;
    private final GameLabel lblStart;
    private final GameMenuPanel gameMenuPanel;

    public GamePanel() {
        super();

        forestX = new int[2];
        Assets assets = new Assets();
        dino = new Dino(assets.getDino());
        levelBackground = assets.getLevelBackground();
        gameSound = GameSound.getInstance();
        gameMenuPanel = new GameMenuPanel(this);
        gameSound.play(GameSound.TRACK.GRASSLAND_THEME);
        lblStart = new GameLabel("Press space bar to start", Config.WIDTH / 2 - 150, 150);

        enemies = EnemyUtil.initEnemies();
        forestX[1] = forestX[0] + levelBackground.getWidth();
        gameMenuPanel.showMenu(GameMenuPanel.Menu.MAIN);
        dino.setListener(this::showRestartMenuWithDelay);
        addKeyListener(new Inputs(this, null));

        requestFocus();
        setLayout(null);
        add(gameMenuPanel);
        setFocusable(true);
        setPreferredSize(new Dimension(Config.WIDTH, Config.HEIGHT));
    }

    public void startGame() {
        requestFocus();
        setFocusable(true);
        forestSpeed = 1;
        isPlayPressed = true;
        lblStart.show(true);
        dino.setState(Dino.WALKING);
        Game.setState(Game.State.READY_TO_START);
    }

    public void restartGame() {
        enemies = EnemyUtil.initEnemies();
        forestX[0] = 0;
        forestX[1] = forestX[0] + levelBackground.getWidth();
        dino.resetPos();
        startGame();
    }

    private void showRestartMenuWithDelay() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gameMenuPanel.showMenu(GameMenuPanel.Menu.RESTART);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        /* Draw Forest */
        g.drawImage(levelBackground, forestX[0], 0, levelBackground.getWidth(), Config.HEIGHT, this);
        g.drawImage(levelBackground, forestX[1], 0, levelBackground.getWidth(), Config.HEIGHT, this);

        dino.draw(g2d, this); /* Draw Dino */
        for (Enemy enemy : enemies) enemy.draw(g2d, this); /* Draw Enemies */
        g.setColor(Color.BLACK);
        lblStart.draw(g);

        /* Draw Score Label */
        g.setColor(Color.BLACK);
        g.setFont(GameFont.getInstance().getSuperDream());
        g.drawString(String.valueOf(score), 50, 50);
    }

    public void update() {
        dino.animate();

        switch (Game.getCurrentState()) {
            case Game.State.READY_TO_START -> {
                if (!gameMenuPanel.isMenuVisible()) {
                    moveBackground(forestSpeed);
                }
            }
            case Game.State.RUNNING -> {
                moveBackground(forestSpeed);
                /* To move all the enemies */
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy e = enemies.get(i);
                    e.animate();
                    e.update(forestSpeed);
                    EnemyUtil.checkCollision(dino, e);
                    if (e.hasEnemyMovedOutOffScreen()) {
                        enemies.remove(e);
                        enemies.add(new Enemy(EnemyUtil.getRandomEnemyType(), Config.WIDTH + getRandomSpacing()));
                        i--;
                    }
                }
            }
            case Game.State.RESTART -> {

            }
            case PAUSED, OVER -> {
                if (enemyCausedDeath != null) enemyCausedDeath.animate();
            }
            case null -> {/*System.out.println("Invalid state");*/}
        }
    }

    private void moveBackground(int speed) {
        forestX[0] = forestX[0] - speed;
        forestX[1] = forestX[1] - speed;

        if (forestX[0] + levelBackground.getWidth() <= 0) {
            forestX[0] = forestX[1] + levelBackground.getWidth();
        }
        if (forestX[1] + levelBackground.getWidth() <= 0) {
            forestX[1] = forestX[0] + levelBackground.getWidth();
        }
    }

    @Override
    public void onSpaceBarPressed() {
        //System.out.println(core.Game.getCurrentState() + "? " + isPlayPressed);
        if (!Game.isGameReadyToStart && !Game.isGameRunning) return; /* Only two game state where the dino can jump */
        if (isPlayPressed) {
            forestSpeed = 2;
            isPlayPressed = false;
            lblStart.show(false);
            dino.setState(Dino.RUNNING);
            Game.setState(Game.State.RUNNING);
            //gameSound.play(GameSound.TRACK.RUN);
        } else {
            dino.setState(Dino.JUMPING);
        }
    }

    @Override
    public void onEscapeKeyPressed() {
        if (!Game.isGamePaused && Game.isGameRunning) {
            Game.isGamePaused = true;
            gameMenuPanel.showMenu(GameMenuPanel.Menu.PAUSE);
        } else if (Game.isGamePaused && Game.isGameRunning) {
            Game.isGamePaused = false;
            gameMenuPanel.showMenu(GameMenuPanel.Menu.NOMENU);
        }
    }
}
