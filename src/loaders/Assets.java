package loaders;

import entites.Dino;

import java.awt.image.BufferedImage;

public class Assets {

    private final BufferedImage[][] dinoAnimMap;
    private final BufferedImage[] levelBackground;

    public Assets() {
        dinoAnimMap = new BufferedImage[Dino.NUM_STATES][];
        levelBackground = ImageLoader.loadFolderImg("/levels/");

        loadDino();
    }

    public void loadDino() {
        dinoAnimMap[Dino.IDLE] = ImageLoader.loadFolderImg("/t_rex/idle");
        dinoAnimMap[Dino.WALKING] = ImageLoader.loadFolderImg("/t_rex/walking");
        dinoAnimMap[Dino.RUNNING] = ImageLoader.loadFolderImg("/t_rex/running");
        dinoAnimMap[Dino.JUMPING] = ImageLoader.loadFolderImg("/t_rex/jumping");
        dinoAnimMap[Dino.DEATH] = ImageLoader.loadFolderImg("/t_rex/death");
    }

    public BufferedImage[][] getDino() {
        return dinoAnimMap;
    }

    public BufferedImage getLevelBackground() {
        return levelBackground[1];
    }
}
