import javax.swing.*;

public class Game extends JFrame implements Runnable {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;
    public static final int FPS = 120;

    public enum Menu {
        MAIN, OPTIONS, NOMENU
    }

    private boolean isGameOver;
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
//        thread.stop();
        isGameOver = true;
    }

    @Override
    public void run() {

        long currNano;
        int frames = 0;
        long lastFrame = System.nanoTime();
        double timePerFrame = 1000000000.0 / FPS;
        long lastTimeInMillis = System.currentTimeMillis();

        while (!isGameOver) {
            currNano = System.nanoTime();
            if (currNano - lastFrame >= timePerFrame) {
                frames++;
                gamePanel.repaint();
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
