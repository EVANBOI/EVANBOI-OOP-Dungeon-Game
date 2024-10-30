package dungeonmania.entities.enemies.movement;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class RandomMovement implements EnemyMovement {
    private Random randGen = new Random();

    @Override
    public Position getNextEnemyPos(GameMap map, Enemy enemy) {
        Position nextPos;
        List<Position> pos = enemy.getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> map.canMoveTo(enemy, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            nextPos = enemy.getPosition();
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
        }
        return nextPos;
    }

}
