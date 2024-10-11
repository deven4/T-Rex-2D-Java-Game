package Utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;

public class GameSound {

    private static GameSound gameSound = null;
    private Clip introClip;
    private Clip btnHoverClip;

    private GameSound() {
        try {
            InputStream inputStream1 = getClass().getResourceAsStream("/sounds/button_hover.wav");
            InputStream inputStream2 = getClass().getResourceAsStream("/sounds/dino_intro.wav");
            assert inputStream1 != null;
            assert inputStream2 != null;
            AudioInputStream audioInputStream1 = AudioSystem.getAudioInputStream(inputStream1);
            AudioInputStream audioInputStream2 = AudioSystem.getAudioInputStream(inputStream2);
            introClip = AudioSystem.getClip();
            introClip.open(audioInputStream2);
            btnHoverClip = AudioSystem.getClip();
            btnHoverClip.open(audioInputStream1);
        } catch (Exception exception) {
            System.err.println("Error: " + exception.getClass().getName() + " - " + exception.getMessage());
        }
    }

    public static synchronized GameSound getInstance() {
        if (gameSound == null) gameSound = new GameSound();
        return gameSound;
    }

    public Clip getIntroClip() {
        return introClip;
    }

    public Clip getBtnHoverClipSound() {
        return btnHoverClip;
    }
}
