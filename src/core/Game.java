package core;

import utils.Config;

import javax.swing.*;
import java.util.Arrays;

public class Game extends JFrame implements Runnable {

    public enum State {
        READY_TO_START, RUNNING, PAUSED, RESTART, OVER
    }

    public static boolean isGameOver;
    public static boolean isGamePaused;
    public static boolean isGameRunning;
    public static boolean isGameRestart;
    public static boolean isGameReadyToStart;

    private GamePanel gamePanel;
    private boolean isGameLoopRunning;

    public Game() {
        SwingUtilities.invokeLater(() -> {
//            try {
                gamePanel = new GamePanel();
                add(gamePanel);
                pack();
                setVisible(true);
                setResizable(false);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                startGame();
//            } catch (Exception e) {
//                // Log the error and stop the game
//                System.err.println(Arrays.toString(e.getStackTrace()));
//                JOptionPane.showMessageDialog(null, e.getMessage(),
//                        "Error", JOptionPane.ERROR_MESSAGE);
//                System.exit(1);
//            }
        });
    }

    private void startGame() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        long currNano;
        int frames = 0;
        long lastFrame = System.nanoTime();
        double timePerFrame = 1_000_000_000.0 / Config.FPS;
        long lastTimeInMillis = System.currentTimeMillis();

        while (!isGameLoopRunning) {
            currNano = System.nanoTime();
            if (currNano - lastFrame >= timePerFrame) {
                frames++;
                if (!isGamePaused) {
                    gamePanel.update();            // <-- critical for animation/movement
                    gamePanel.repaint();      // draw the updated state
                }
                lastFrame = currNano;
            }

            if (System.currentTimeMillis() - lastTimeInMillis >= 1000) {
                lastTimeInMillis = System.currentTimeMillis();
                // System.out.println("FPS: " + frames);
                frames = 0;
            }

            try {
                //noinspection BusyWait
                Thread.sleep(1); // prevent 100% CPU usage
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static void setState(State gameState) {
        switch (gameState) {
            case READY_TO_START -> {
                isGameOver = false;
                isGamePaused = false;
                isGameRunning = false;
                isGameRestart = false;
                isGameReadyToStart = true;
            }
            case RUNNING -> {
                isGameOver = false;
                isGamePaused = false;
                isGameRestart = false;
                isGameReadyToStart = false;
                isGameRunning = true;
            }
            case PAUSED -> {
                isGameOver = false;
                isGameRunning = false;
                isGameRestart = false;
                isGameReadyToStart = false;
                isGamePaused = true;
            }
            case RESTART -> {
                isGameOver = false;
                isGamePaused = false;
                isGameRunning = false;
                isGameReadyToStart = false;
                isGameRestart = true;
            }
            case OVER -> {
                isGamePaused = false;
                isGameRunning = false;
                isGameRestart = false;
                isGameReadyToStart = false;
                isGameOver = true;
            }
        }
    }

    public static State getCurrentState() {
        if (isGameReadyToStart) return State.READY_TO_START;
        if (isGameRunning) return State.RUNNING;
        if (isGamePaused) return State.PAUSED;
        if (isGameRestart) return State.RESTART;
        if (isGameOver) return State.OVER;
        return null;
    }

    public static void main(String[] args) {
        new Game();
    }
}
