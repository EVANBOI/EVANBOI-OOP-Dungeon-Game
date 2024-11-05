package dungeonmania.entities.logicals;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class CurrentSubject extends Entity {
    private List<CurrentObserver> observers = new ArrayList<>();
    private boolean activated = false;

    public CurrentSubject(Position position) {
        super(position);
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public List<CurrentObserver> getObservers() {
        return observers;
    }

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

    public void detach(CurrentObserver observer) {
        observers.remove(observer);
        observer.detachFromSubject(this, activated);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public void notifyCardinallyAdjacentExcept(String id, boolean current) {
        for (CurrentObserver obs : observers) {
            if (!obs.getId().equals(id)) {
                obs.updateCurrent(this, current);
            }
        }
    }
}
