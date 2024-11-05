package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.inventory.UseableBuffItem;

import java.util.List;

public class Bow extends UseableBuffItem implements Buildable {
    public Bow(int durability) {
        super(null, durability);
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, 0, 2, 1));
    }

    public static boolean isBuildable(Inventory inventory) {
        int numOfWood = inventory.count(Wood.class);
        int numOfArrows = inventory.count(Arrow.class);
        return numOfWood >= 1 && numOfArrows >= 3;
    }

    @Override
    public InventoryItem buildItem(Inventory inventory, boolean remove, EntityFactory factory) {
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Arrow> arrows = inventory.getEntities(Arrow.class);

        if (isBuildable(inventory)) {
            if (remove) {
                inventory.remove(wood.get(0));
                inventory.remove(arrows.get(0));
                inventory.remove(arrows.get(1));
                inventory.remove(arrows.get(2));
            }
            return factory.buildBow();
        }
        return null;
    }

    public static String stringValue() {
        return "bow";
    }

}
