package dungeonmania.entities.enemies;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.movement.SpiderMovement;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Spider extends Enemy {
    public static final int DEFAULT_SPAWN_RATE = 0;
    public static final double DEFAULT_ATTACK = 5;
    public static final double DEFAULT_HEALTH = 10;

    public Spider(Position position, double health, double attack) {
        super(position.asLayer(Entity.DOOR_LAYER + 1), health, attack);
        setMovement(new SpiderMovement(position));
    };

    @Override
    public void move(Game game) {
        GameMap map = game.getMap();

        Position nextPos = getMovement().getNextEnemyPos(map, this);
        List<Entity> entities = game.getMap().getEntities(nextPos);
        if (entities == null || entities.size() == 0 || entities.stream().allMatch(e -> e.canMoveOnto(map, this))) {
            map.moveTo(this, nextPos);
        }
    }
}
