import Utils.GameFont;
import Utils.Inputs;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel implements Inputs.Listener {

    private Dino dino;
    private Inputs inputs;
    private ForestBG forest;
    private GameFont gameFont;
    private Button btnPlay, btnOptions, btnExit, btnSound,
            btnBack, btnMusicOn, btnMusicOff, btnMusic;
    private Font btnFont;
    private BufferedImage forestImg;

    private int dinoImgIdx;
    private int currState;
    private int animationTick;
    private int animationSpeed;
    private int[] forestX;
    private int dinoY;
    private int forestSpeed = 1;
    private int textX, textY;

    private boolean isGameStarted;

    public GamePanel() {
        Initialise();
    }

    private void Initialise() {
        animationSpeed = 12;
        forestX = new int[2];
        currState = Dino.IDLE;
        dinoY = Dino.Y_COORDINATE;
        textX = (int) (Game.WIDTH - Game.WIDTH * 0.40); // subtracting 40% from the gameBoard width
        textY = (int) (Game.HEIGHT - Game.HEIGHT * 0.80);

        initClasses();

        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        addKeyListener(inputs);
    }

    private void initClasses() {
        dino = new Dino();
        forest = new ForestBG();
        gameFont = new GameFont();
        btnFont = gameFont.getSugarDeath2();
        inputs = new Inputs(this, null);
        forestImg = forest.getBufferedImage()[0];
        forestX[1] = forestX[0] + forestImg.getWidth();

        /* Buttons */
        btnPlay = new Button(this, "PLAY", new Point(textX, textY), btnFont);
        btnOptions = new Button(this, "OPTIONS", new Point(textX, textY + 80), btnFont);
        btnExit = new Button(this, "EXIT", new Point(textX, textY + 160), btnFont);

        /* Options menu */
        btnBack = new Button(this, "< BACK", new Point(textX - 120, textY), btnFont);
        btnSound = new Button(this, "SOUND", new Point(textX - 100, textY + 80), btnFont);
        Point ptMusicOn = new Point(textX - 80 + btnSound.getWidth(), textY + 80);
        btnMusicOn = new Button(this, "<", ptMusicOn, gameFont.getBananaYeti());
        btnMusic = new Button(this, "ON", new Point(btnMusicOn.getCoordinates().x + 40,
                btnMusicOn.getCoordinates().y), btnFont);
        btnMusicOff = new Button(this, ">", new Point(btnMusic.getCoordinates().x + 100,
                btnMusic.getCoordinates().y), gameFont.getBananaYeti());

        btnMusic.isPlainText(true);
        btnSound.isPlainText(true);

        showMenu(Game.Menu.MAIN);

        btnPlay.setListener(this::startGame);
        btnOptions.setListener(() -> {
            showMenu(Game.Menu.OPTIONS);
        });
        btnExit.setListener(() -> {
            System.exit(0);
        });
        btnBack.setListener(() -> {
            showMenu(Game.Menu.MAIN);
        });
        btnMusicOn.setListener(() -> {
            if (btnMusic.getText().equals("OFF"))
                btnMusic.setText("ON");
        });
        btnMusicOff.setListener(() -> {
            if (btnMusic.getText().equals("ON"))
                btnMusic.setText("OFF");
        });
    }

    private void startGame() {
        btnPlay.setAnimationSpeed(20);
        btnOptions.setAnimationSpeed(20);
        btnExit.setAnimationSpeed(20);

        btnPlay.hide(true);
        btnOptions.hide(true);
        btnExit.hide(true);

        isGameStarted = true;
        currState = Dino.WALKING;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.setColor(Color.RED);

        move();
        g.drawImage(forestImg, forestX[0], 0, forestImg.getWidth(), forestImg.getHeight(), this);
        g.drawImage(forestImg, forestX[1], 0, forestImg.getWidth(), forestImg.getHeight(), this);

        g.drawImage(dino.getImages()[currState][dinoImgIdx], Dino.X_COORDINATE, dinoY,
                150, 120, null);

        drawText(g);
    }

    private void drawText(Graphics g) {
        btnBack.draw(g);
        btnSound.draw(g);
        btnMusicOn.draw(g);
        btnMusic.draw(g);
        btnMusicOff.draw(g);

        btnPlay.draw(g);
        btnOptions.draw(g);
        btnExit.draw(g);

       /* btnSound.drawRect(g);
        btnMusicOn.drawRect(g);
        btnMusic.drawRect(g);
        btnMusicOff.drawRect(g);*/

        /*
        g.drawRect(btnPlay.madar().x, btnPlay.madar().y, btnPlay.madar().width, btnPlay.madar().height);
        g.drawRect(btnOptions.madar().x, btnOptions.madar().y, btnOptions.madar().width, btnOptions.madar().height);
        g.drawRect(btnExit.madar().x, btnExit.madar().y, btnExit.madar().width, btnExit.madar().height);
         */
    }

    private void move() {
        animationTick++;

        if (isGameStarted) moveBackground();
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
        if (dinoY <= 200) dinoY = Dino.Y_COORDINATE;
        if (currState == Dino.JUMPING && dinoImgIdx > 3) {
            dinoY -= 1;
        }

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

    public void showMenu(Game.Menu menu) {
        switch (menu) {
            case MAIN -> {
                btnPlay.hide(false);
                btnOptions.hide(false);
                btnExit.hide(false);
                btnBack.hide(true);
                btnSound.hide(true);
                btnMusicOn.hide(true);
                btnMusic.hide(true);
                btnMusicOff.hide(true);
            }
            case OPTIONS -> {
                btnOptions.hide(true);
                btnPlay.hide(true);
                btnExit.hide(true);
                btnSound.hide(false);
                btnBack.hide(false);
                btnMusicOn.hide(false);
                btnMusic.hide(false);
                btnMusicOff.hide(false);
            }
        }
    }

    @Override
    public void onUpPressed(int value) {
        List<String> hello = new ArrayList<>();

        if (currState == Dino.WALKING) {
            currState = Dino.RUNNING;
            forestSpeed = 2;
        } else {
            currState = Dino.WALKING;
            forestSpeed = 1;
        }

        dinoImgIdx = 0;
    }

    @Override
    public void onSpaceBarPressed() {
        if (currState != Dino.JUMPING) {
            currState = Dino.JUMPING;
            dinoImgIdx = 0;
            forestSpeed = 1;
        }
    }
}
