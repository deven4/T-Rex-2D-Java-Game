import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Obstacles {

    private BufferedImage cactusImg;

    public Obstacles() {
        InputStream inputStream = getClass().getResourceAsStream("obstacles/Catcus.png");
        try {
            assert inputStream != null;
            cactusImg = ImageIO.read(inputStream);
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    public BufferedImage getCactusImage() {
        return cactusImg;
    }
}
