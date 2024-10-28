package dungeonmania.entities.enemies.movement;

import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class ToPlayerMovement implements EnemyMovement {
    @Override
    public Position getNextEnemyPos(GameMap map, Enemy enemy) {
        Player player = map.getPlayer();
        Position nextPos = map.dijkstraPathFind(enemy.getPosition(), player.getPosition(), enemy);
        return nextPos;
    }

}
