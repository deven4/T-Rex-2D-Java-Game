package utils;

import java.awt.*;

public class Config {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;
    public static final int FPS = 150;

    // subtracting 40% from the gameBoard width
    public static final int MENU_X = (int) (Config.WIDTH - Config.WIDTH * 0.44);
    public static final int MENU_Y = (int) (Config.HEIGHT - Config.HEIGHT * 0.80) + 20;

    public static final String enemyDir = "/enemies";

    public static final Color PRIMARY_COLOR = Color.ORANGE;
    public static final Color SECONDARY_COLOR = Color.BLACK;
    public static int unitSize = 10;

    public static final int IDLE = 100;
    public static final int WALK = 101;
}
