import Entites.Assets;
import Entites.Dino;
import Entites.Enemy;
import Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements Inputs.Listener {

    public static int score;

    private int forestSpeed;
    private final int[] forestX;
    private boolean isPlayPressed;
    private final BufferedImage forestImg;

    private final Assets assets;
    private final Dino dino;
    private final GameSound gameSound;
    private final GameLabel lblStart;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final GameMenuPanel gameMenuPanel;
    private final BufferedImage[][] enemiesGrp = new BufferedImage[2][];
    private final Random random;

    public GamePanel() throws Exception {
        super();

        forestX = new int[2];
        random = new Random();
        assets = new Assets();
        dino = new Dino(assets.getDino());
        forestImg = new ForestBG().getBufferedImage()[1];
        gameSound = GameSound.getInstance();
        gameMenuPanel = new GameMenuPanel(this);
        gameSound.play(GameSound.TRACK.GRASSLAND_THEME);
        lblStart = new GameLabel("Press space bar to start", Config.WIDTH / 2 - 150, 150);

        initEnemies();
        forestX[1] = forestX[0] + forestImg.getWidth();
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
        forestX[0] = 0;
        forestX[1] = forestX[0] + forestImg.getWidth();
        initEnemies();
        dino.resetPos();
        for (Enemy enemy : enemies) enemy.resetPosition();
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
        g.drawImage(forestImg, forestX[0], 0, forestImg.getWidth(), Config.HEIGHT, this);
        g.drawImage(forestImg, forestX[1], 0, forestImg.getWidth(), Config.HEIGHT, this);

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
                    if (e.hasEnemyMovedOutOffScreen()) {
                        enemies.remove(e);
                        addEnemy(Config.WIDTH + getRandomSpacing());
                        i--;
                    } else if (e.collision(dino.getY(), dino.getImageSize()) && dino.getCurrentState() != Dino.DEATH) {
                        /* To check collision of all the enemies */
                        gameSound.play(GameSound.TRACK.DEATH);
                        dino.setState(Dino.DEATH);
                        Game.setState(Game.State.OVER);
                        return;
                    }
                }
                System.out.println();
                for (Enemy e : enemies) System.out.print(e.getTag() + " x:" + e.getX() + ",");
            }
            case Game.State.RESTART -> {
                System.out.println("anim");
                for (Enemy enemy : enemies) enemy.animate();
            }
            case PAUSED, OVER -> {
            }
            case null -> {/*System.out.println("Invalid state");*/}
        }
    }

    private void moveBackground(int speed) {
        forestX[0] = forestX[0] - speed;
        forestX[1] = forestX[1] - speed;

        if (forestX[0] + forestImg.getWidth() <= 0) {
            forestX[0] = forestX[1] + forestImg.getWidth();
        }
        if (forestX[1] + forestImg.getWidth() <= 0) {
            forestX[1] = forestX[0] + forestImg.getWidth();
        }
    }

    private void initEnemies() {
        enemies.clear();
        enemiesGrp[0] = assets.getCactus();
        enemiesGrp[1] = assets.getSkeletonBombImages();

        int spawnX = Config.WIDTH;
        for (int i = 0; i < 3; i++) {
            addEnemy(spawnX);
            spawnX += getRandomSpacing();
        }
        System.out.println("START");
        for (Enemy e : enemies) System.out.print(e.getTag() + " x:" + e.getX() + ",");
    }

    private void addEnemy(int spawnX) {
        int randomIdx = random.nextInt(enemiesGrp.length);
        try {
            Enemy enemy;
            switch (randomIdx) {
                case 1 -> {
                    enemy = new Enemy(enemiesGrp[randomIdx], spawnX, Dino.Y_COORDINATE + 30);
                    enemy.setTag("Bomb");
                }
                default -> {
                    enemy = new Enemy(enemiesGrp[randomIdx], spawnX);
                    enemy.setTag("Cactus");
                }
            }
            enemies.add(enemy);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getFarthestEnemyX() {
        return enemies.stream().mapToInt(Enemy::getX).max().orElse(Config.WIDTH);
    }

    public int getRandomSpacing() {
        return 300 + new Random().nextInt(150, 250); // 250–300 pixels
    }

    @Override
    public void onSpaceBarPressed() {
        //System.out.println(Game.getCurrentState() + "? " + isPlayPressed);
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
        }
    }
}
