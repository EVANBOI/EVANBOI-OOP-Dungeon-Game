package dungeonmania.entities.buildables;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public interface Buildable {
    public InventoryItem buildItem(Inventory inventory, boolean remove, EntityFactory factory);

}
