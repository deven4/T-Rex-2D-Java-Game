import Utils.GameConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Dino {

    public static final int WALKING = 0;
    public static final int RUNNING = 1;
    public static final int JUMPING = 2;
    public static final int IDLE = 3;
    public static final int DEATH = 4;

    public static final int NUM_STATES = 5;
    public static final int MAX_IMAGES_PER_STATE = 12;

    public static final int X_COORDINATE = GameConfig.WIDTH / 4;
    public static final int Y_COORDINATE = 360;

    private final int[][] stateLength;
    private int imageWidth, imageHeight;
    private final BufferedImage[][] bufferedImage;
    private final HashMap<Integer, Point> imageDimensions;

    public Dino() throws Exception {
        stateLength = new int[NUM_STATES][1];
        imageDimensions = new HashMap<>();
        bufferedImage = new BufferedImage[NUM_STATES][MAX_IMAGES_PER_STATE];

        loadImage(getStateName(IDLE), IDLE);
        loadImage(getStateName(WALKING), WALKING);
        loadImage(getStateName(RUNNING), RUNNING);
        loadImage(getStateName(JUMPING), JUMPING);
        loadImage(getStateName(DEATH), DEATH);
    }

    private void loadImage(String directory, int state) throws Exception {
        URL urlResources = getClass().getResource(STR."/t_rex/\{directory}");
        assert urlResources != null;
        Path path = Paths.get(urlResources.toURI());
        File dir = new File(String.valueOf(path.toFile()));
        File[] allFiles = dir.listFiles();
        if (allFiles == null) {
            System.err.println(STR."No Files found inside: \{dir}");
            return;
        }

        imageWidth = 0;
        imageHeight = 0;
        stateLength[state][0] = allFiles.length;
        for (int i = 0; i < MAX_IMAGES_PER_STATE; i++) {
            if (i >= allFiles.length) break;
            bufferedImage[state][i] = ImageIO.read(allFiles[i]);

            if (imageDimensions.get(state) != null) {
                if ((imageDimensions.get(state).x != bufferedImage[state][i].getWidth()
                        && imageDimensions.get(state).x != 0)
                        || (imageDimensions.get(state).y != bufferedImage[state][i].getHeight()
                        && imageDimensions.get(state).y != 0))
                    throw new IOException("All Dino images are not of the same size! " +
                            "Please reinstall the application.");
            }

            imageWidth = bufferedImage[state][i].getWidth();
            imageHeight = bufferedImage[state][i].getHeight();
            imageDimensions.computeIfAbsent(state, k -> new Point(imageWidth, imageHeight));
            //System.out.println("File: " + allFiles[i].getPath());
        }
    }

    private String getStateName(int state) {
        switch (state) {
            case WALKING -> {
                return "walking";
            }
            case RUNNING -> {
                return "running";
            }
            case JUMPING -> {
                return "jumping";
            }
            case DEATH -> {
                return "death";
            }
            case IDLE -> {
                return "idle";
            }
            default -> {
                return "";
            }
        }
    }

    public Point getImageHeightWidth(int state) throws Exception {
        if (imageDimensions.get(state) == null) throw new Exception("No Image dimensions found for the state");
        return imageDimensions.get(state);
    }

    public BufferedImage[][] getImages() {
        return bufferedImage;
    }

    public int getStateLength(int state) {
        return stateLength[state][0];
    }
}
