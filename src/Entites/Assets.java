package Entites;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Assets {

    private BufferedImage[] cactusImageArray;
    private final BufferedImage[][] bufferedImage;

    public Assets() throws Exception {
        bufferedImage = new BufferedImage[Dino.NUM_STATES][];

        loadDino("/t_rex/idle", Dino.IDLE);
        loadDino("/t_rex/walking", Dino.WALKING);
        loadDino("/t_rex/running", Dino.RUNNING);
        loadDino("/t_rex/jumping", Dino.JUMPING);
        loadDino("/t_rex/death", Dino.DEATH);

        loadCactus();
    }

    private File[] loadImages(String directory) throws Exception {
        URL urlResources = getClass().getResource(directory);
        assert urlResources != null;
        Path path = Paths.get(urlResources.toURI());
        File dir = new File(String.valueOf(path.toFile()));
        File[] allFiles = dir.listFiles();
        if (allFiles == null) {
            System.err.println("No Files found inside: " + dir);
            return null;
        }
        return allFiles;
    }

    public void loadDino(String directory, int state) throws Exception {
        File[] files = loadImages(directory);
        assert files != null;
        bufferedImage[state] = new BufferedImage[files.length];
        for (int i = 0; i < files.length; i++) {
            bufferedImage[state][i] = ImageIO.read(files[i]);

//            if (imageDimensions.get(state) != null) {
//                if ((imageDimensions.get(state).x != bufferedImage[state][i].getWidth()
//                        && imageDimensions.get(state).x != 0)
//                        || (imageDimensions.get(state).y != bufferedImage[state][i].getHeight()
//                        && imageDimensions.get(state).y != 0))
//                    throw new IOException("All Entites.Dino images are not of the same size! " +
//                            "Please reinstall the application.");
//            }
        }

        //System.out.println(directory + ":" + bufferedImage[state].length);
    }

    private void loadCactus() throws Exception {
        File[] files = loadImages("/obstacles");
        assert files != null;
        cactusImageArray = new BufferedImage[files.length];
        for (int i = 0; i < files.length; i++) {
            cactusImageArray[i] = ImageIO.read(files[i]);
        }
    }

    public BufferedImage[][] getDino() {
        return bufferedImage;
    }

    public BufferedImage[] getCactus() {
        return cactusImageArray;
    }
}
