package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.OverlapBehaviour;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Key extends InventoryItem implements OverlapBehaviour {
    private int number;

    public Key(Position position, int number) {
        super(position);
        this.number = number;
    }

    public void onOverlap(GameMap map, Entity entity) {
        CollectableOnOverlap.handleOverlap(map, entity, this);
    }

    public int getnumber() {
        return number;
    }
}
