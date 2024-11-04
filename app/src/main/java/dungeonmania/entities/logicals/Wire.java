package dungeonmania.entities.logicals;

import java.util.ArrayList;
import java.util.List;
import dungeonmania.entities.logicals.logics.Logic;
import dungeonmania.entities.logicals.logics.Or;
import dungeonmania.map.GameMap;
import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.util.Position;

public class Wire extends Entity implements CurrentObserver, CurrentSubject {
    private List<CurrentObserver> observers = new ArrayList<>();
    private Logic logic = new Or();
    private boolean activeCurrent = false;

    public Wire(Position position) {
        super(position);
    }

    @Override
    public void attach(CurrentObserver observer, GameMap map) {
        observers.add(observer);
        observer.attachToSubject(this, activeCurrent);
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
        observer.detachFromSubject(this, activeCurrent);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void updateCurrent(CurrentSubject subject, boolean current) {
        logic.updateNumActivated(subject, current);
        boolean currentLastTick = activeCurrent;
        activeCurrent = logic.checkIfFulfilled();
        if (activeCurrent != currentLastTick) {
            notifyCardinallyAdjacentExcept(subject.getId(), activeCurrent);
        }
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
    public void attachToSubject(CurrentSubject subject, boolean current) {
        logic.updateAdjacentConductorsAttachment(subject, current);
    }

    @Override
    public void detachFromSubject(CurrentSubject subject, boolean current) {
        logic.updateAdjacentConductorsDetachment(subject, current);
    }
}
