package Utils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class GameFont {

    private Font spiritFont;
    private Font lemonTuesday;
    private Font bananaYeti;
    private Font sugarDeath2;
    private Font superDream;

    public GameFont() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            InputStream stream1 = getClass().getResourceAsStream("/fonts/fightingSpiritTBS.ttf");
            InputStream stream2 = getClass().getResourceAsStream("/fonts/lemonTuesday.otf");
            InputStream stream3 = getClass().getResourceAsStream("/fonts/bananaYeti_Extrabold.ttf");
            InputStream stream4 = getClass().getResourceAsStream("/fonts/sugarDeath2.ttf");
            InputStream stream5 = getClass().getResourceAsStream("/fonts/superDream.ttf");

            assert stream1 != null;
            spiritFont = Font.createFont(Font.TRUETYPE_FONT, stream1).deriveFont(45f);
            assert stream2 != null;
            lemonTuesday = Font.createFont(Font.TRUETYPE_FONT, stream2).deriveFont(45f);
            assert stream3 != null;
            bananaYeti = Font.createFont(Font.TRUETYPE_FONT, stream3).deriveFont(45f);
            assert stream4 != null;
            sugarDeath2 = Font.createFont(Font.TRUETYPE_FONT, stream4).deriveFont(45f);
            assert stream5 != null;
            superDream = Font.createFont(Font.TRUETYPE_FONT, stream5).deriveFont(45f);
            ge.registerFont(spiritFont);
        } catch (FontFormatException | IOException | AssertionError exception) {
            System.err.println("Fonts not loaded: " + exception);
        }
    }

    public Font getSuperDream() {
        return superDream;
    }

    public Font getFightingSpirit() {
        return spiritFont;
    }

    public Font getLemonTuesday() {
        return lemonTuesday;
    }

    public Font getBananaYeti() {
        return bananaYeti;
    }

    public Font getSugarDeath2() {
        return sugarDeath2;
    }
}
