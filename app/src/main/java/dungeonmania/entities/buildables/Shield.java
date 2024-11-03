package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Useable;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;
import java.util.List;

public class Shield extends Buildable implements Useable {
    private int durability;
    private double defence;

    public Shield(int durability, double defence) {
        super(null);
        this.durability = durability;
        this.defence = defence;
    }

    @Override
    public void use(Game game) {
        durability--;
        if (durability <= 0) {
            game.getPlayer().remove(this);
        }
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, defence, 1, 1));
    }

    @Override
    public int getDurability() {
        return durability;
    }

    public static boolean isBuildable(Inventory inventory) {
        int numOfWood = inventory.count(Wood.class);
        int numOfTreasure = inventory.count(Treasure.class);
        int numOfKeys = inventory.count(Key.class);
        return numOfWood >= 2 && (numOfTreasure >= 1 || numOfKeys >= 1);
    }

    public Buildable buildItem(Inventory inventory, boolean remove, EntityFactory factory) {
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Treasure> treasure = inventory.getEntities(Treasure.class);
        List<Key> keys = inventory.getEntities(Key.class);

        if (isBuildable(inventory)) {
            if (remove) {
                inventory.remove(wood.get(0));
                inventory.remove(wood.get(1));
                if (treasure.size() >= 1) {
                    inventory.remove(treasure.get(0));
                } else {
                    inventory.remove(keys.get(0));
                }
            }
            return factory.buildShield();
        }
        return null;
    }

    public static String stringValue() {
        return "shield";
    }

}
