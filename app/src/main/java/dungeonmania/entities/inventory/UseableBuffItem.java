package dungeonmania.entities.inventory;

import dungeonmania.util.Position;

public abstract class UseableBuffItem extends BuffInventoryItem {
    public UseableBuffItem(Position position) {
        super(position);
    }

    public abstract int getDurability();
}
