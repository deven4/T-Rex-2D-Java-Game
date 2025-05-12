import Entites.Assets;
import Entites.Dino;
import Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class GamePanel extends JPanel implements Inputs.Listener {

    public static int score;

    private final int[] forestX;
    private int forestSpeed = 1;
    private boolean isPlayPressed;
    private final BufferedImage forestImg;

    private final Dino dino;
    private final GameSound gameSound;
    private final GameLabel lblStart;
    private final GameMenuPanel gameMenuPanel;
    private final ArrayList<Enemy> enemyList = new ArrayList<>();

    public GamePanel() throws Exception {
        super();
        forestX = new int[2];
        Assets assets = new Assets();
        dino = new Dino(assets.getDino());
        forestImg = new ForestBG().getBufferedImage()[1];
        gameSound = GameSound.getInstance();
        gameMenuPanel = new GameMenuPanel(this);
        gameSound.play(GameSound.TRACK.GRASSLAND_THEME);
        lblStart = new GameLabel("Press space bar to start", Config.WIDTH / 2 - 150, 150);

        forestX[1] = forestX[0] + forestImg.getWidth();
        gameMenuPanel.showMenu(GameMenuPanel.Menu.MAIN);
        enemyList.add(new Enemy(assets.getCactus()));
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
        lblStart.show(true);
        isPlayPressed = true;
        dino.setState(Dino.WALKING);
        Game.setState(Game.State.READY_TO_START);
    }

    public void restartGame() {
        dino.setState(Dino.IDLE);
        Game.setState(Game.State.RESTART);
        forestX[0] = 0;
        forestX[1] = forestX[0] + forestImg.getWidth();
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
        for (Enemy enemy : enemyList) enemy.draw(g2d, this); /* Draw Cactus */
        g.setColor(Color.BLACK);
        lblStart.draw(g);

        /* Draw Score Label */
        g.setColor(Color.BLACK);
        g.setFont(GameFont.getInstance().getSuperDream());
        g.drawString(String.valueOf(score), 50, 50);
    }

    public void update() {
        dino.animate();

        if (Game.isGameReadyToStart) {
            if (!gameMenuPanel.isMenuVisible()) moveBackground(forestSpeed);
        } else if (Game.isGameRunning) {
            moveBackground(forestSpeed);
            /* To move all the enemies */
            for (Enemy enemy : enemyList) {
                enemy.animate();
                /* To check collision of all the enemies */
                if (enemy.collision(dino.getY(), dino.getImageSize())
                        && dino.getCurrentState() != Dino.DEATH) {
                    gameSound.play(GameSound.TRACK.DEATH);
                    dino.setState(Dino.DEATH);
                    Game.setState(Game.State.OVER);
                    return;
                }
                if (enemy.isEnemyCrossed) continue;
                enemy.move(forestSpeed);
                break;
            }
        } else if (Game.isGameRestart) {
           // moveBackground(-3);
        } else if (Game.isGameOver) {

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

    @Override
    public void onSpaceBarPressed() {
        if (!Game.isGameReadyToStart && !Game.isGameRunning) return; /* Only two game state where the dino can jump */

        if (isPlayPressed) {
            forestSpeed = 2;
            isPlayPressed = false;
            lblStart.show(false);
            dino.setState(Dino.RUNNING);
            Game.setState(Game.State.RUNNING);
            //gameSound.play(GameSound.TRACK.RUN);
        } else {
            //counter = 0;
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
