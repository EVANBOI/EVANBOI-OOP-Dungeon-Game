package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;

public class CollectableOnOverlap {
    public static void handleOverlap(GameMap map, Entity entity, InventoryItem item) {
        if (entity instanceof Player) {
            if (!((Player) entity).pickUp(item))
                return;
            map.destroyEntity(item);
        }
    }
}
