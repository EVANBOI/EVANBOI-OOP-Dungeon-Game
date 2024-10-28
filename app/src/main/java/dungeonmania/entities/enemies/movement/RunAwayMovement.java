package dungeonmania.entities.enemies.movement;

import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class RunAwayMovement implements EnemyMovement {
    @Override
    public Position getNextEnemyPos(GameMap map, Enemy enemy) {
        Player player = map.getPlayer();
        Position plrDiff = Position.calculatePositionBetween(player.getPosition(), enemy.getPosition());

        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(enemy.getPosition(), Direction.RIGHT)
                : Position.translateBy(enemy.getPosition(), Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(enemy.getPosition(), Direction.DOWN)
                : Position.translateBy(enemy.getPosition(), Direction.UP);
        Position offset = enemy.getPosition();
        if (plrDiff.getY() == 0 && map.canMoveTo(enemy, moveX))
            offset = moveX;
        else if (plrDiff.getX() == 0 && map.canMoveTo(enemy, moveY))
            offset = moveY;
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            if (map.canMoveTo(enemy, moveX))
                offset = moveX;
            else if (map.canMoveTo(enemy, moveY))
                offset = moveY;
            else
                offset = enemy.getPosition();
        } else {
            if (map.canMoveTo(enemy, moveY))
                offset = moveY;
            else if (map.canMoveTo(enemy, moveX))
                offset = moveX;
            else
                offset = enemy.getPosition();
        }
        Position nextPos = offset;
        return nextPos;
    }

}
