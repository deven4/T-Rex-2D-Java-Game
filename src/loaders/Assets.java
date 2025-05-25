package loaders;

import entites.Player;

import java.awt.image.BufferedImage;

public class Assets {

    private final BufferedImage[][] dinoAnimMap;
    private final BufferedImage[][] robotAnimMap;
    private final BufferedImage[] levelBackground;

    public Assets() {
        dinoAnimMap = new BufferedImage[Player.NUM_STATES][];
        robotAnimMap = new BufferedImage[Player.NUM_STATES][];
        levelBackground = ImageLoader.loadFolderImg("/levels/");

        loadDino();
        loadRobot();
    }

    public void loadDino() {
        dinoAnimMap[Player.IDLE] = ImageLoader.loadFolderImg("/players/t_rex/idle");
        dinoAnimMap[Player.WALKING] = ImageLoader.loadFolderImg("/players/t_rex/walk");
        dinoAnimMap[Player.RUNNING] = ImageLoader.loadFolderImg("/players/t_rex/running");
        dinoAnimMap[Player.JUMPING] = ImageLoader.loadFolderImg("/players/t_rex/jumping");
        dinoAnimMap[Player.DEATH] = ImageLoader.loadFolderImg("/players/t_rex/death");
    }

    private void loadRobot() {
        robotAnimMap[Player.IDLE] = ImageLoader.loadFolderImg("/players/robot/idle");
    }

    public BufferedImage[][] getDino() {
        return dinoAnimMap;
    }

    public BufferedImage[][] getRobotAnimMap() {
        return robotAnimMap;
    }

    public BufferedImage getLevelBackground() {
        return levelBackground[1];
    }
}
