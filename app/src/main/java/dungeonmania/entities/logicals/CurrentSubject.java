package dungeonmania.entities.logicals;

import java.util.List;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public interface CurrentSubject {
    public void attach(CurrentObserver observer, GameMap map);

    public void detach(CurrentObserver observer);

    public void notifyCardinallyAdjacentExcept(String id, boolean current);

    public String getId();

    public List<Position> getCardinallyAdjacentPositions();
}
