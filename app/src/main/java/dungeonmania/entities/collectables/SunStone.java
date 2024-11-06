package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.OverlapBehaviour;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SunStone extends InventoryItem implements OverlapBehaviour {
    public SunStone(Position position) {
        super(position);
    }

    public void onOverlap(GameMap map, Entity entity) {
        CollectableOnOverlap.handleOverlap(map, entity, this);
    }
}
