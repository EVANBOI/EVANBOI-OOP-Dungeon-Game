package dungeonmania.entities;

import java.util.ArrayList;

import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.logicals.CurrentObserver;
import dungeonmania.entities.logicals.CurrentSubject;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Switch extends CurrentSubject implements OverlapBehaviour, MovedAwayBehaviour, OnDestroyBehaviour {
    public Switch(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER));
    }

    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            setActivated(true);
            notifyCardinallyAdjacentExcept(null, true);
        }
        updateLogicals(map);
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            setActivated(false);
            notifyCardinallyAdjacentExcept(null, false);
        }
        updateLogicals(map);
    }

    @Override
    public void onDestroy(GameMap gameMap) {
        setActivated(false);
        notifyCardinallyAdjacentExcept(null, false);
        new ArrayList<>(getObservers()).stream().forEach(obs -> detach(obs));
        updateLogicals(gameMap);
    }

    private void updateLogicals(GameMap map) {
        map.getEntities().stream().filter(e -> e instanceof CurrentObserver).map(e -> (CurrentObserver) e)
                .forEach(l -> l.updateConditionStatus());

        map.getEntities().stream().filter(e -> e instanceof Bomb).map(e -> (Bomb) e).filter(Bomb::willExplode)
                .forEach(b -> b.bombDetonation(map));
    }
}
