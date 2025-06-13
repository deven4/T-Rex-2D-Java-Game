package core;

import entites.Enemy;
import entites.Player;
import entites.PlayerType;
import loaders.Assets;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import static core.Game.State.READY_TO_RUN;
import static core.Game.State.START;
import static utils.EnemyUtil.getRandomSpacing;

public class GamePanel extends JPanel {

    public static int score;

    private int forestSpeed;
    private int[] forestX;
    private BufferedImage levelBackground;

    public Assets assets;
    public Player player;
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
        assets = new Assets();
        GameSound gameSound = GameSound.getInstance();
        levelBackground = assets.getLevelBackground();
        gameMenuPanel = new GameMenuPanel(this, inputManager);
        gameSound.play(GameSound.TRACK.GRASSLAND_THEME);
        lblStart = new GameLabel("Press space bar to jump", Config.WIDTH / 2 - 150, 150);

        enemies = EnemyUtil.initEnemies();
        forestX[1] = forestX[0] + levelBackground.getWidth();
        gameMenuPanel.showMenu(GameMenuPanel.Menu.MAIN);

        switchPlayer(PlayerType.T_REX);
    }

    public void startGame() {
        requestFocus();
        setFocusable(true);
        forestSpeed = 1;
        lblStart.show(true);
        player.setState(Player.WALKING);
        Game.setState(READY_TO_RUN);
    }

    public void resetPanel() {
        enemies = EnemyUtil.initEnemies();
        forestX[0] = 0;
        forestX[1] = forestX[0] + levelBackground.getWidth();
        player.resetPos();
        player.setState(Player.IDLE);
        Game.setState(START);
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
        System.out.println(Game.getCurrState());

        switch (Game.getCurrState()) {
            case START, OVER -> player.update();
            case READY_TO_RUN -> {
                player.update();
                if (!gameMenuPanel.isMenuVisible()) {
                    moveBackground(forestSpeed);
                    checkSpaceBarClick();
                }
            }
            case RUNNING -> {
                player.update();
                checkSpaceBarClick();
                checkEscapeClicked();
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
            case PAUSED -> {
            }
            case RESTART -> {

            }
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

    private void checkEscapeClicked() {
        if (!Game.isState(Game.State.RUNNING) && !Game.isState(Game.State.PAUSED)) return;

        if (inputManager.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            if (Game.isState(Game.State.RUNNING)) {
                Game.setState(Game.State.PAUSED);
                gameMenuPanel.showMenu(GameMenuPanel.Menu.PAUSE);
            } else if (Game.isState(Game.State.PAUSED)) {
                Game.setState(Game.State.RUNNING);
                gameMenuPanel.showMenu(GameMenuPanel.Menu.NOMENU);
            }
        }
    }

    private void checkSpaceBarClick() {
        /* Only two game state where the dino can jump */
        if (!Game.isState(READY_TO_RUN) && !Game.isState(Game.State.RUNNING)) return;
        if (inputManager.isKeyPressed(KeyEvent.VK_SPACE)) {
            forestSpeed = 2;
            lblStart.show(false);
            Game.setState(Game.State.RUNNING);
            player.setState(Player.JUMPING);
        }
    }

    public void switchPlayer(PlayerType type) {
        switch (type) {
            case T_REX -> player = new Player(assets.getDino());
            case ROBOT -> player = new Player(assets.getRobot(), 8, Player.Y_COORDINATE + 10);
        }

        player.setListener(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            gameMenuPanel.showMenu(GameMenuPanel.Menu.RESTART);
        });
    }
}
