package utils;

import core.Game;
import entites.Player;
import entites.Enemy;
import entites.EnemyType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyUtil {

    private static final Random random = new Random();

    public static EnemyType getRandomEnemyType() {
        EnemyType[] values = EnemyType.values();
        return values[random.nextInt(values.length)];
    }

    public static List<Enemy> initEnemies() {
        List<Enemy> enemies = new ArrayList<>();

        int spawnX = Config.WIDTH;
        for (int i = 0; i < 3; i++) {
            spawnX += getRandomSpacing();
            enemies.add(new Enemy(getRandomEnemyType(), spawnX));
        }

        return enemies;
    }

    public static int getRandomSpacing() {
        return 300 + new Random().nextInt(150, 250); // 250–300 pixels
    }

    public static void checkCollision(Player player, Enemy e) {
        if (!e.isAlive() || e.isDying()) return;

        if (player.getBounds().intersects(e.getBounds())) {
            Rectangle dinoBounds = player.getBounds();
            Rectangle enemyBounds = e.getBounds();
            //System.out.println((dinoBounds.y + dinoBounds.height - 5) + " < " + enemyBounds.y);
            boolean isJumpKill = player.getVelocity() > 0;

            if (isJumpKill && e.getType() == EnemyType.SKELETON_BOMB) {
                e.die();
            } else {
                GameSound.getInstance().play(GameSound.TRACK.DEATH);
                player.setState(Player.DEATH);
                Game.setState(Game.State.OVER);
            }
        }
    }
}
