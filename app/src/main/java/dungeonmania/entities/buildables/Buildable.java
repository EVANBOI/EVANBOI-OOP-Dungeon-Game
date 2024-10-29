package dungeonmania.entities.buildables;

import dungeonmania.entities.inventory.ConsumableInventoryItem;
import dungeonmania.util.Position;

public abstract class Buildable extends ConsumableInventoryItem {
    public Buildable(Position position) {
        super(position);
    }
}
