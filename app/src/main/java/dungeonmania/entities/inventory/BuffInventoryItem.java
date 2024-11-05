package dungeonmania.entities.inventory;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.util.Position;

public abstract class BuffInventoryItem extends InventoryItem {
    public BuffInventoryItem(Position position) {
        super(position);
    }

    public abstract BattleStatistics applyBuff(BattleStatistics origin);
}
