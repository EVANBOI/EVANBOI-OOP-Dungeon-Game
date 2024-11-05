package dungeonmania.entities.logicals;

import dungeonmania.entities.logicals.logics.Logic;
import dungeonmania.entities.logicals.logics.Or;
import dungeonmania.util.Position;

public class Wire extends CurrentSubject implements CurrentObserver {
    private Logic logic = new Or();

    public Wire(Position position) {
        super(position);
    }

    @Override
    public void updateCurrent(CurrentSubject subject, boolean current) {
        logic.updateNumActivated(subject, current);
        boolean currentLastTick = isActivated();
        updateConditionStatus();
        if (isActivated() != currentLastTick) {
            notifyCardinallyAdjacentExcept(subject.getId(), isActivated());
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

    @Override
    public void updateConditionStatus() {
        setActivated(logic.checkIfFulfilled());
    }
}
