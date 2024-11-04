package dungeonmania.entities.logicals;

import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.entities.logicals.logics.Logic;
import dungeonmania.entities.logicals.logics.LogicFactory;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwitchDoor extends Entity implements CurrentObserver, Logical {
    private Logic logic;
    private boolean open = false;

    public SwitchDoor(Position position, String logic) {
        super(position);
        this.logic = LogicFactory.getLogic(logic);
    }

    public void updateCurrent(CurrentSubject subject, boolean current) {
        logic.updateNumActivated(subject, current);
    }

    public void updateConditionStatus() {
        open = logic.checkIfFulfilled();
    }

    public boolean isOpen() {
        return open;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return (open || entity instanceof Spider);
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
