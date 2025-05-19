package loaders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageLoader {

    private static File[] loadImages(String directory) throws Exception {
        URL urlResources = ImageLoader.class.getResource(directory);
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

    public static BufferedImage[] loadFolderImg(String directory) {
        try {
            File[] files = loadImages(directory);
            assert files != null;
            BufferedImage[] images = new BufferedImage[files.length];
            for (int i = 0; i < files.length; i++) {
                images[i] = ImageIO.read(files[i]);
            }
            return images;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
