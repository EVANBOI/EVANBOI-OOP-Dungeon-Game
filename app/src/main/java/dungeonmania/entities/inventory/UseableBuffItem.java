package dungeonmania.entities.inventory;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.Useable;
import dungeonmania.util.Position;

public abstract class UseableBuffItem extends InventoryItem implements Useable, BattleItem {
    private int durability;

    public UseableBuffItem(Position position, int durability) {
        super(position);
        this.durability = durability;
    }

    public int getDurability() {
        return durability;
    }

    public void use(Game game) {
        durability--;
        if (durability <= 0) {
            game.getPlayer().remove(this);
        }
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public abstract BattleStatistics applyBuff(BattleStatistics origin);

}
