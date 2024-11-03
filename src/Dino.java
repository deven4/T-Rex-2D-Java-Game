import Utils.GameConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Dino {

    public static final int WALKING = 0;
    public static final int RUNNING = 1;
    public static final int JUMPING = 2;
    public static final int IDLE = 3;
    public static final int DEAD = 4;

    public static final int NUM_STATES = 4;
    public static final int MAX_IMAGES_PER_STATE = 12;

    public static final int X_COORDINATE = GameConfig.WIDTH / 4;
    public static final int Y_COORDINATE = 380;

    private final int[][] stateLength;
    private final BufferedImage[][] bufferedImage;

    public Dino() {
        stateLength = new int[NUM_STATES][1];
        bufferedImage = new BufferedImage[NUM_STATES][MAX_IMAGES_PER_STATE];

        loadImage(getStateName(WALKING), WALKING);
        loadImage(getStateName(RUNNING), RUNNING);
        loadImage(getStateName(JUMPING), JUMPING);
        loadImage(getStateName(IDLE), IDLE);
       // loadImage(getStateName(DEAD), DEAD);
    }

    private void loadImage(String directory, int state) {
        URL urlResources = getClass().getResource("/t_rex/" + directory);
        if (urlResources == null) {
            System.err.println("Resources file not found");
            return;
        }

        try {
            Path path = Paths.get(urlResources.toURI());
            File dir = new File(String.valueOf(path.toFile()));
            File[] allFiles = dir.listFiles();
            if (allFiles == null) {
                System.err.println("No Files found inside: " + dir);
                return;
            }

            stateLength[state][0] = allFiles.length;
            for (int i = 0; i < MAX_IMAGES_PER_STATE; i++) {
                if(i >= allFiles.length) break;
                bufferedImage[state][i] = ImageIO.read(allFiles[i]);
                //System.out.println("File: " + allFiles[i].getPath());
            }
        } catch (URISyntaxException | IOException e) {
            System.out.println("Exception: " + e);
        }
    }

    private String getStateName(int state) {
        switch(state) {
            case WALKING -> {
                return "walking";
            }
            case RUNNING -> {
                return "running";
            }
            case JUMPING -> {
                return "jumping";
            }
            case DEAD -> {
                return "dead";
            }
            case IDLE -> {
                return "idle";
            }
            default -> {
                return "";
            }
        }
    }

    public BufferedImage[][] getImages() {
        return bufferedImage;
    }

    public int getStateLength(int state) {
        return stateLength[state][0];
    }
}
