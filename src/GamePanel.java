import Utils.GameConfig;
import Utils.GameLabel;
import Utils.GameSound;
import Utils.Inputs;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Inputs.Listener {

    private final int[] forestX;
    private int dinoImgIdx;
    private int currState;
    private final int dinoJumpSpeed;
    private int animationTick;
    private final Dino dino;
    int counter = 0;
    private final int animationSpeed;
    private int dinoY;
    private int forestSpeed = 1;
    private boolean isPlayPressed;
    private boolean isGameStarted;
    private int obstacleX;
    private final Clip introClip;
    private final BufferedImage forestImg;
    private boolean dinoTouchedTop;
    private boolean isShowObstacles;
    private int velocity = -20;
    private final GameLabel lblStart;
    private final Obstacles obstacles;
    private final GameMenuPanel gameMenuPanel;

    public GamePanel() {
        super();
        dino = new Dino();
        forestX = new int[2];
        obstacles = new Obstacles();
        forestImg = new ForestBG().getBufferedImage()[1];
        forestX[1] = forestX[0] + forestImg.getWidth();
        animationSpeed = 12;
        dinoJumpSpeed = 3;
        obstacleX = GameConfig.WIDTH;
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        move();

        /* Draw Forest, Dino & Obstacle */
        g.drawImage(forestImg, forestX[0], 0, forestImg.getWidth(), GameConfig.HEIGHT, this);
        g.drawImage(forestImg, forestX[1], 0, forestImg.getWidth(), GameConfig.HEIGHT, this);
        g.drawImage(dino.getImages()[currState][dinoImgIdx], Dino.X_COORDINATE, dinoY,
                120, 100, null);
        g.drawImage(obstacles.getCactusImage(), obstacleX, Dino.Y_COORDINATE,
                obstacles.getCactusImage().getWidth(), obstacles.getCactusImage().getHeight(), null);
        lblStart.draw(g);

        g.setColor(Color.red);
//        g.drawRect(obstacleX, Dino.Y_COORDINATE,
//                obstacles.getCactusImage().getWidth(), obstacles.getCactusImage().getHeight());
    }

    private void move() {
        animationTick++;

        if (isGameStarted) {
            moveBackground();
            if (isShowObstacles) {
                if (obstacleX + obstacles.getCactusImage().getWidth() < 0) obstacleX = GameConfig.WIDTH;
                obstacleX = obstacleX - forestSpeed;
            }
        }
        moveDino();
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
    }

    private void moveDino() {
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
        counter += 1;
        System.out.println(dinoY + "," + dinoImgIdx);

//            if (dinoTouchedTop) {
//                dinoY = dinoY + dinoJumpSpeed + 5;
//                if (dinoY >= Dino.Y_COORDINATE) {
//                    dinoY = Dino.Y_COORDINATE;
//                    currState = Dino.RUNNING;
//                }
//            } else {
//                if (dinoY <= 200) dinoTouchedTop = true;
//                dinoY -= dinoJumpSpeed;
//            }
//            if(dinoY <= 200) dinoY = dinoY + dinoJumpSpeed;
//            else dinoY = dinoY - dinoJumpSpeed;
//            if(dinoY == Dino.Y_COORDINATE) currState = Dino.RUNNING;
        if (animationTick >= animationSpeed) {
            animationTick = 0;

            if (dinoImgIdx >= dino.getStateLength(currState) - 1) {
                if (currState == Dino.JUMPING) {
                    currState = Dino.RUNNING;
                    dinoY = Dino.Y_COORDINATE;
                }
                dinoImgIdx = 0;
            } else dinoImgIdx++;
        }
    }

    @Override
    public void onUpPressed(int value) {
       /* List<String> hello = new ArrayList<>();

        if (currState == Dino.WALKING) {
            currState = Dino.RUNNING;
            forestSpeed = 2;
        } else {
            currState = Dino.WALKING;
            forestSpeed = 1;
        }

        dinoImgIdx = 0;
        */
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
            dinoTouchedTop = false;
            velocity = -20;
            counter = 0;
            currState = Dino.JUMPING;
            dinoImgIdx = 0;
        }
    }

    @Override
    public void onEscapeKeyPressed() {
        Game.isGamePaused = true;
        gameMenuPanel.showMenu(GameMenuPanel.Menu.PAUSE);
    }
}
