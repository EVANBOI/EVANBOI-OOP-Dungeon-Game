package dungeonmania.entities.buildables;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.inventory.ConsumableInventoryItem;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.util.Position;

public abstract class Buildable extends ConsumableInventoryItem {
    public Buildable(Position position) {
        super(position);
    }

    public abstract Buildable buildItem(Inventory inventory, boolean remove, EntityFactory factory);

    public abstract boolean isBuildable(Inventory inventory);

    public abstract String toString();
}
