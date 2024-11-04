package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.logicals.CurrentObserver;
import dungeonmania.entities.logicals.CurrentSubject;
import dungeonmania.entities.logicals.Logical;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Switch extends Entity implements OverlapBehaviour, MovedAwayBehaviour, CurrentSubject, OnDestroyBehaviour {
    private List<CurrentObserver> observers = new ArrayList<>();
    private boolean activated;

    public Switch(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER));
    }

    @Override
    public void attach(CurrentObserver observer, GameMap map) {
        observers.add(observer);
        observer.attachToSubject(this, activated);
        if (observer instanceof Bomb) {
            Bomb b = (Bomb) observer;
            b.updateConditionStatus();
            if (b.willExplode()) {
                b.bombDetonation(map);
            }
        }
    }

    @Override
    public void detach(CurrentObserver observer) {
        observers.remove(observer);
        observer.detachFromSubject(this, activated);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = true;
            notifyCardinallyAdjacentExcept(null, activated);
        }

        map.getEntities().stream().filter(e -> e instanceof Logical).map(e -> (Logical) e)
                .forEach(l -> l.updateConditionStatus());

        map.getEntities().stream().filter(e -> e instanceof Bomb).map(e -> (Bomb) e).filter(Bomb::willExplode)
                .forEach(b -> b.bombDetonation(map));

    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = false;
            notifyCardinallyAdjacentExcept(null, activated);
        }
        updateLogicals(map);
    }

    public boolean isActivated() {
        return activated;
    }

    @Override
    public void notifyCardinallyAdjacentExcept(String id, boolean current) {
        for (CurrentObserver obs : observers) {
            if (!obs.getId().equals(id)) {
                obs.updateCurrent(this, current);
            }
        }
    }

    @Override
    public void onDestroy(GameMap gameMap) {
        activated = false;
        notifyCardinallyAdjacentExcept(null, activated);
        updateLogicals(gameMap);
        new ArrayList<>(observers).stream().forEach(obs -> detach(obs));
    }

    private void updateLogicals(GameMap map) {
        map.getEntities().stream().filter(e -> e instanceof Logical).map(e -> (Logical) e)
                .forEach(l -> l.updateConditionStatus());

        map.getEntities().stream().filter(e -> e instanceof Bomb).map(e -> (Bomb) e).filter(Bomb::willExplode)
                .forEach(b -> b.bombDetonation(map));
    }
}
