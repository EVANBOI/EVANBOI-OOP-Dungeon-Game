package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.inventory.UseableBuffItem;

import java.util.List;

public class Shield extends UseableBuffItem implements Buildable {
    private double defence;

    public Shield(int durability, double defence) {
        super(null, durability);
        this.defence = defence;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, defence, 1, 1));
    }

    public static boolean isBuildable(Inventory inventory) {
        int numOfWood = inventory.count(Wood.class);
        int numOfTreasure = inventory.count(Treasure.class);
        int numOfKeys = inventory.count(Key.class);
        int numOfSunStones = inventory.count(SunStone.class);
        return (numOfWood >= 2 && (numOfTreasure >= 1 || numOfKeys >= 1 || numOfSunStones >= 1));
    }

    public InventoryItem buildItem(Inventory inventory, boolean remove, EntityFactory factory) {
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Treasure> treasure = inventory.getEntities(Treasure.class);
        List<Key> keys = inventory.getEntities(Key.class);

        if (remove) {
            inventory.remove(wood.get(0));
            inventory.remove(wood.get(1));
            if (treasure.size() >= 1) {
                inventory.remove(treasure.get(0));
            } else if (keys.size() >= 1) {
                inventory.remove(keys.get(0));
            }
        }
        return factory.buildShield();
    }

    public static String stringValue() {
        return "shield";
    }

}
