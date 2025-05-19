package animations;


import entites.EnemyState;
import loaders.ImageLoader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AnimationManager {

    private final Map<EnemyState, Animation> animations;
    private static final Map<String, Map<EnemyState, Animation>> cache = new HashMap<>();

    public AnimationManager(String enemyType) {
        if (!cache.containsKey(enemyType)) {
            cache.put(enemyType, loadAnimations(enemyType));
        }
        animations = cache.get(enemyType);
    }

    private Map<EnemyState, Animation> loadAnimations(String enemyType) {
        Map<EnemyState, Animation> animMap = new HashMap<>();

        for (EnemyState state : EnemyState.values()) {
            String dir = "/enemies/" + enemyType.toLowerCase() + "/" + state.name().toLowerCase();
            if (AnimationManager.class.getResource(dir) == null) continue;

            BufferedImage[] frames = ImageLoader.loadFolderImg(dir);
            boolean looping = (state == EnemyState.IDLE || state == EnemyState.WALK);
            animMap.put(state, new Animation(frames, looping));
        }
        return animMap;
    }

    public Animation getAnimation(EnemyState state) {
        return animations.get(state);
    }
}