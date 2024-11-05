package dungeonmania.entities.logicals;

import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwitchDoor extends UncollectableLogicEntity {
    public SwitchDoor(Position position, String logic) {
        super(position, logic);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return (isActivated() || entity instanceof Spider);
    }
}
