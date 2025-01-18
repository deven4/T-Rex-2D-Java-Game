package Utils;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.util.Objects;

public class GameSound {

    public enum TRACK {
        INTRO, GRASSLAND, DEATH
    }

    private boolean isSoundOnInMenu;
    private Player player;
    private Thread playerThread;
    private String grasslandStr = "/sounds/grasslands_theme.mp3";
    private String introStr = "/sounds/intro_theme.mp3";
    private String deathStr = "/sounds/death.ogg";

    private static GameSound gameSound = null;

    private GameSound() throws Exception {
        isSoundOnInMenu = true;
        try {
            getClass().getResourceAsStream("/sounds/button_hover.wav");
            getClass().getResourceAsStream(introStr);
            getClass().getResourceAsStream(grasslandStr);
            getClass().getResourceAsStream(deathStr);
        } catch (Exception exception) {
            throw new Exception("Unable to load audio - " + exception.getClass() + ": " + exception.getMessage());
        }
    }

    public static synchronized GameSound getInstance() throws Exception {
        if (gameSound == null) gameSound = new GameSound();
        return gameSound;
    }

    public void playClip(TRACK track) {
        if (!isSoundOnInMenu) return;

        // Stop any currently playing audio immediately by interrupting its thread
        if (playerThread != null && playerThread.isAlive()) {
            playerThread.interrupt(); // Interrupt the current playback thread
            //  stopPlayer(); // Stop the audio playback
        }

        playerThread = new Thread(() -> {
            stopPlayer();
            try {
                BufferedInputStream bufferedInputStream;
                if (track == TRACK.INTRO)
                    bufferedInputStream = new BufferedInputStream(Objects.requireNonNull(getClass().getResourceAsStream(introStr)));
                else if (track == TRACK.GRASSLAND)
                    bufferedInputStream = new BufferedInputStream(Objects.requireNonNull(getClass().getResourceAsStream(grasslandStr)));
                else
                    bufferedInputStream = new BufferedInputStream(Objects.requireNonNull(getClass().getResourceAsStream(deathStr)));

                Thread.sleep(100);
                player = new Player(bufferedInputStream);
                player.play();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        playerThread.start();
    }

    public void isSoundOn(boolean isSoundOnInMenu) {
        this.isSoundOnInMenu = isSoundOnInMenu;
    }

    public void stopPlayer() {
        if (player != null) {
            player.close();
            //player = null;
        }
    }
}
