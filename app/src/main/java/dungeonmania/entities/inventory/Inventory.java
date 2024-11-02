package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.Buildable;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Useable;
import java.util.Arrays;

public class Inventory {
    private List<InventoryItem> items = new ArrayList<>();

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public List<String> getBuildables() {

        List<String> result = new ArrayList<>();
        List<Buildable> possibleItems = Arrays.asList(new Bow(0), new Shield(0, 0));

        for (Buildable buildable : possibleItems) {
            if (buildable.isBuildable(this)) {
                result.add(buildable.toString());
            }
        }
        return result;
    }

    public InventoryItem checkBuildCriteria(Player p, boolean remove, String item, EntityFactory factory) {

        List<Buildable> possibleItems = Arrays.asList(new Bow(0), new Shield(0, 0));

        for (Buildable buildable : possibleItems) {
            if (buildable.toString().equals(item)) {
                return buildable.buildItem(this, remove, factory);
            }
        }
        return null;
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId))
                return (Entity) item;
        return null;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null;
    }

    public Useable getWeapon() {
        Useable weapon = getFirst(Sword.class);
        if (weapon == null)
            return getFirst(Bow.class);
        return weapon;
    }

}
