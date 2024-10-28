package dungeonmania.entities.enemies.movement;

import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public interface EnemyMovement {
    public Position getNextEnemyPos(GameMap map, Enemy enemy);
}
