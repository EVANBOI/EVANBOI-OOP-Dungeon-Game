package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.inventory.BattleItem;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;

public class MidnightArmour extends InventoryItem implements Buildable, BattleItem {
    private double attack;
    private double defence;

    public MidnightArmour(double attack, double defence) {
        super(null);
        this.defence = defence;
        this.attack = attack;
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, this.attack, this.defence, 1, 1));
    }

    public static boolean isBuildable(Inventory inventory, GameMap map) {
        int numOfSunStones = inventory.count(SunStone.class);
        int numOfSwords = inventory.count(Sword.class);
        if (map.getEntities(ZombieToast.class).size() >= 1) {
            return false;
        }
        return numOfSwords >= 1 && numOfSunStones >= 1;
    }

    public InventoryItem buildItem(Inventory inventory, boolean remove, EntityFactory factory) {
        List<SunStone> sunStones = inventory.getEntities(SunStone.class);
        List<Sword> swords = inventory.getEntities(Sword.class);

        if (remove) {
            inventory.remove(sunStones.get(0));
            inventory.remove(swords.get(0));
        }
        return factory.buildMidnightArmour();
    }

    public static String stringValue() {
        return "midnight_armour";
    }

}
