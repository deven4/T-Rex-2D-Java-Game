import Utils.GameConfig;

import javax.swing.*;

public class Game extends JFrame implements Runnable {

    public static boolean isGameOver;
    public static boolean isGamePaused;
    private final GamePanel gamePanel;

    public Game() {
        gamePanel = new GamePanel();

        add(gamePanel);
        pack();
        setTitle("Dino");
        setVisible(true);
        setResizable(false);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        startGame();
    }

    private void startGame() {
        Thread thread = new Thread(this);
        thread.start();
    }

    private void stopGame() {
        isGameOver = true;
    }

    @Override
    public void run() {

        long currNano;
        int frames = 0;
        long lastFrame = System.nanoTime();
        double timePerFrame = 1000000000.0 / GameConfig.FPS;
        long lastTimeInMillis = System.currentTimeMillis();

        while (!isGameOver) {
            currNano = System.nanoTime();
            if (currNano - lastFrame >= timePerFrame) {
                frames++;
                if (!isGamePaused) gamePanel.repaint();
                lastFrame = currNano;
            }

            if(System.currentTimeMillis() - lastTimeInMillis >= 1000) {
                lastTimeInMillis = System.currentTimeMillis();
              //  System.out.println("Frames per second: " + frames);
                frames = 0;
            }
        }
    }

    public static void main(String[] args) {
        new Game();
    }
}
