package core;

import utils.Config;

import javax.swing.*;

public class Game extends JFrame implements Runnable {

    public enum State {
        START, READY_TO_RUN, RUNNING, PAUSED, RESTART, OVER
    }

    private GamePanel gamePanel;
    private boolean isGameLoopRunning;
    private static State currState = State.START;

    public Game() {
        SwingUtilities.invokeLater(() -> {
            gamePanel = new GamePanel();
            setContentPane(gamePanel);
            pack();
            setVisible(true);
            setResizable(false);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            startGame();
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
                gamePanel.update();            // <-- critical for animation/movement
                gamePanel.repaint();      // draw the updated state

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

    public static void setState(State state) {
        currState = state;
    }

    public static State getCurrState() {
        return currState;
    }

    public static boolean isState(State state) {
        return getCurrState() == state;
    }

    public static void main(String[] args) {
        new Game();
    }
}


//            } catch (Exception e) {
//                // Log the error and stop the game
//                System.err.println(Arrays.toString(e.getStackTrace()));
//                JOptionPane.showMessageDialog(null, e.getMessage(),
//                        "Error", JOptionPane.ERROR_MESSAGE);
//                System.exit(1);
//            }