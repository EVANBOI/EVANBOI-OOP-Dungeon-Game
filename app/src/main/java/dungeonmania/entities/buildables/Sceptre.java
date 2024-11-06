package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.collectables.Key;

public class Sceptre extends InventoryItem implements Buildable {
    private int duration;

    public Sceptre(int duration) {
        super(null);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public static String stringValue() {
        return "sceptre";
    }

    public static boolean isBuildable(Inventory inventory) {
        int numOfWood = inventory.count(Wood.class);
        int numOfArrows = inventory.count(Arrow.class);
        int numOfSunStones = inventory.count(SunStone.class);
        int numOfTreasure = inventory.count(Treasure.class);
        int numOfKeys = inventory.count(Key.class);

        return (numOfWood >= 1 || numOfArrows >= 2) && (numOfKeys >= 1 || numOfTreasure >= 1) && (numOfSunStones >= 1)
                || (numOfWood >= 1 || numOfArrows >= 2) && (numOfSunStones >= 2);
    }

    public InventoryItem buildItem(Inventory inventory, boolean remove, EntityFactory factory) {
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Arrow> arrows = inventory.getEntities(Arrow.class);
        List<SunStone> sunStones = inventory.getEntities(SunStone.class);
        List<Treasure> treasure = inventory.getEntities(Treasure.class);
        List<Key> keys = inventory.getEntities(Key.class);

        if (remove) {
            if (wood.size() >= 1) {
                inventory.remove(wood.get(0));
            } else {
                inventory.remove(arrows.get(0));
                inventory.remove(arrows.get(1));
            }
            if (keys.size() >= 1) {
                inventory.remove(keys.get(0));
            } else if (treasure.size() >= 1) {
                inventory.remove(treasure.get(0));
            }
            inventory.remove(sunStones.get(0));
        }
        return factory.buildSceptre();
    }

}
