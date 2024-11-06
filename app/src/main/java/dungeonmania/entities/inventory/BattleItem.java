package dungeonmania.entities.inventory;

import dungeonmania.battles.BattleStatistics;

public interface BattleItem {
    public BattleStatistics applyBuff(BattleStatistics origin);
}
