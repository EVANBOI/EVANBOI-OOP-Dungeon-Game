package dungeonmania.entities.logicals;

import dungeonmania.entities.Entity;
import dungeonmania.entities.logicals.logics.Logic;
import dungeonmania.entities.logicals.logics.LogicFactory;
import dungeonmania.util.Position;

public class LightBulb extends Entity implements CurrentObserver, Logical {
    private Logic logic;
    private boolean on = false;

    public LightBulb(Position position, String logic) {
        super(position);
        this.logic = LogicFactory.getLogic(logic);
    }

    public void updateCurrent(CurrentSubject subject, boolean current) {
        logic.updateNumActivated(subject, current);
    }

    public void updateConditionStatus() {
        on = logic.checkIfFulfilled();
    }

    public boolean isOn() {
        return on;
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
