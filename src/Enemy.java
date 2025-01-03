import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class Enemy {

    public static final int Y = Dino.Y_COORDINATE + 20;
    private final BufferedImage[] enemyImageArray;

    public Enemy() throws Exception {
        URL url = getClass().getResource("obstacles");
        assert url != null;
        File file = new File(url.toURI());
        File[] fileContent = file.listFiles();
        assert fileContent != null;
        enemyImageArray = new BufferedImage[fileContent.length];
        for (int i = 0; i < fileContent.length; i++) {
            enemyImageArray[i] = ImageIO.read(fileContent[i]);
        }
    }

    public BufferedImage[] getCactusImage() {
        return enemyImageArray;
    }

    public int getWidth() {
        return enemyImageArray[0].getWidth() - 20;
    }
}
