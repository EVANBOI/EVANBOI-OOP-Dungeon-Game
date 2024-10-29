package dungeonmania.entities.inventory;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.util.Position;

public abstract class ConsumableInventoryItem extends InventoryItem {
    public ConsumableInventoryItem(Position position) {
        super(position);
    }

    // @Override
    // public abstract void onOverlap(GameMap map, Entity entity);

    public abstract BattleStatistics applyBuff(BattleStatistics origin);

    public abstract int getDurability();
}
