import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class ForestBG {

    private BufferedImage[] bufferedImage;

    public ForestBG() {
        loadImage();
    }

    private void loadImage() {
        URL resource = getClass().getResource("/levels/");
        if (resource == null) {
            System.err.println("Resources folder `levels` not found");
            return;
        }

        try {
            File folder = Paths.get(resource.toURI()).toFile();
            //System.out.println(resource);
            File[] files = folder.listFiles();
            if(files == null) return;
            bufferedImage = new BufferedImage[files.length];
            for(int i = 0; i< files.length; i++) {
                bufferedImage[i] = ImageIO.read(files[i]);
               // System.out.println(files[i]);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage[] getBufferedImage() {
        return bufferedImage;
    }
}
