package Utils;

import Utils.Sounds.AudioMaster;
import Utils.Sounds.Source;

public class GameSound {

    public enum TRACK {
        INTRO, BUTTON_HOVER, GRASSLAND_THEME, DEATH, RUN
    }

    private final int dinoDeath;
    private final int dinoFootstep;
    private final int buttonHover;
    private final int introTheme;
    private final int grasslandTheme;

    private boolean isSoundOn;

    private final Source sourceRun;
    private final Source sourceDino;
    private final Source sourceIntro;
    private final Source sourceHover;
    private static GameSound gameSound = null;

    public GameSound() {
        try {
            AudioMaster.init();
        } catch (Exception e) {
            System.err.println("OpenAL not loaded successfully!");
        }
        isSoundOn = true;

        sourceDino = new Source(1, 0);
        sourceHover = new Source(1, 0);
        sourceIntro = new Source(0.5f, 0);
        sourceRun = new Source(1, 0);

        dinoDeath = AudioMaster.loadSound("/sounds/death.wav");
        dinoFootstep = AudioMaster.loadSound("/sounds/step_rock.wav");
        buttonHover = AudioMaster.loadSound("/sounds/button_hover.wav");
        introTheme = AudioMaster.loadSound("/sounds/intro_theme.wav");
        grasslandTheme = AudioMaster.loadSound("/sounds/grasslands_theme.wav");
    }

    public static synchronized GameSound getInstance() {
        if (gameSound == null) gameSound = new GameSound();
        return gameSound;
    }

    public void play(TRACK track) {
        if(track == TRACK.BUTTON_HOVER) sourceHover.play(buttonHover);
        if(!isSoundOn) return;

        switch (track) {
            case DEATH -> sourceDino.play(dinoDeath);
            case RUN -> sourceRun.playLooping(dinoFootstep);
            case INTRO -> sourceIntro.play(introTheme);
            case GRASSLAND_THEME -> sourceIntro.play(grasslandTheme);
        }
    }

    public void stop(TRACK track) {
        switch (track) {
            case INTRO, GRASSLAND_THEME -> sourceIntro.stop();
        }
    }

    public void setSoundOn(boolean isSoundOn) {
        this.isSoundOn = isSoundOn;
    }
}
