package dungeonmania.entities;

import dungeonmania.map.GameMap;

public interface OverLappable {
    public void onOverlap(GameMap map, Entity entity);
}
