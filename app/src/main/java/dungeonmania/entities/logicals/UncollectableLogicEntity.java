package dungeonmania.entities.logicals;

import dungeonmania.entities.Entity;
import dungeonmania.entities.logicals.logics.Logic;
import dungeonmania.entities.logicals.logics.LogicFactory;
import dungeonmania.util.Position;

public class UncollectableLogicEntity extends Entity implements CurrentObserver {
    private Logic logic;
    private boolean activated = false;

    public UncollectableLogicEntity(Position position, String logic) {
        super(position);
        this.logic = LogicFactory.getLogic(logic);
    }

    @Override
    public void updateCurrent(CurrentSubject subject, boolean current) {
        logic.updateNumActivated(subject, current);
    }

    @Override
    public void updateConditionStatus() {
        activated = logic.checkIfFulfilled();
    }

    public boolean isActivated() {
        return activated;
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
