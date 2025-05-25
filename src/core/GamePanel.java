package core;

import entites.Enemy;
import entites.Player;
import loaders.Assets;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import static utils.EnemyUtil.getRandomSpacing;

public class GamePanel extends JPanel {

    public static int score;

    private int forestSpeed;
    private int[] forestX;
    private BufferedImage levelBackground;

    private Player player;
    private List<Enemy> enemies;
    private GameLabel lblStart;
    private GameMenuPanel gameMenuPanel;
    InputManager inputManager = new InputManager();

    public GamePanel() {
        super();

        init();
        addKeyListener(inputManager);
        addMouseListener(inputManager);
        addMouseMotionListener(inputManager);

        requestFocus();
        setLayout(null);
        add(gameMenuPanel);
        setFocusable(true);
        setPreferredSize(new Dimension(Config.WIDTH, Config.HEIGHT));
    }

    public void init() {
        forestX = new int[2];
        Assets assets = new Assets();
        GameSound gameSound = GameSound.getInstance();
        player = new Player(assets.getDino());
        levelBackground = assets.getLevelBackground();
        gameMenuPanel = new GameMenuPanel(this, inputManager);
        gameSound.play(GameSound.TRACK.GRASSLAND_THEME);
        lblStart = new GameLabel("Press space bar to jump", Config.WIDTH / 2 - 150, 150);

        enemies = EnemyUtil.initEnemies();
        forestX[1] = forestX[0] + levelBackground.getWidth();
        gameMenuPanel.showMenu(GameMenuPanel.Menu.MAIN);
        player.setListener(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            gameMenuPanel.showMenu(GameMenuPanel.Menu.RESTART);
        });
    }

    public void startGame() {
        requestFocus();
        setFocusable(true);
        forestSpeed = 1;
        lblStart.show(true);
        player.setState(Player.WALKING);
        Game.setState(Game.State.READY_TO_START);
    }

    public void restartGame() {
        enemies = EnemyUtil.initEnemies();
        forestX[0] = 0;
        forestX[1] = forestX[0] + levelBackground.getWidth();
        player.resetPos();
        player.setState(Player.IDLE);
        startGame();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        /* Draw Forest */
        g.drawImage(levelBackground, forestX[0], 0, levelBackground.getWidth(), Config.HEIGHT, this);
        g.drawImage(levelBackground, forestX[1], 0, levelBackground.getWidth(), Config.HEIGHT, this);

        player.draw(g2d, this); /* Draw Dino */
        for (Enemy enemy : enemies) enemy.draw(g2d, this); /* Draw Enemies */
        g.setColor(Color.BLACK);
        lblStart.draw(g);

        /* Draw Score Label */
        g.setColor(Color.BLACK);
        g.setFont(GameFont.getInstance().getSuperDream());
        g.drawString(String.valueOf(score), 50, 50);

        paintChildren(g); // ✨ ADD THIS TO PAINT THE RED PANEL OR ANY CHILD PANELS
        g2d.dispose();
    }

    public void update() {
        player.update();
        gameMenuPanel.update();

        switch (Game.getCurrentState()) {
            case Game.State.READY_TO_START -> {
                if (!gameMenuPanel.isMenuVisible()) {
                    moveBackground(forestSpeed);
                    checkSpaceBarClick();
                }
            }
            case Game.State.RUNNING -> {
                checkSpaceBarClick();
                moveBackground(forestSpeed);
                /* To move all the enemies */
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy e = enemies.get(i);
                    e.animate();
                    e.update(forestSpeed);
                    EnemyUtil.checkCollision(player, e);
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
            }
            case null -> {}
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

    private void checkSpaceBarClick() {
        /* Only two game state where the dino can jump */
        if (!Game.isGameReadyToStart && !Game.isGameRunning) return;
        if (inputManager.isKeyPressed(KeyEvent.VK_SPACE)) {
            forestSpeed = 2;
            lblStart.show(false);
            Game.setState(Game.State.RUNNING);
            player.setState(Player.JUMPING);
        }
    }
}
