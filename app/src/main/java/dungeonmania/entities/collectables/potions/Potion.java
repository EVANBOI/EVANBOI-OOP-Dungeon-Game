package dungeonmania.entities.collectables.potions;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.OverlapBehaviour;
import dungeonmania.entities.collectables.CollectableOnOverlap;
import dungeonmania.entities.inventory.UseableBuffItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Potion extends UseableBuffItem implements OverlapBehaviour {
    private int duration;

    public Potion(Position position, int duration) {
        super(position);
        this.duration = duration;
    }

    public void onOverlap(GameMap map, Entity entity) {
        CollectableOnOverlap.handleOverlap(map, entity, this);
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return origin;
    }

    @Override
    public int getDurability() {
        return 1;
    }
}
