package dungeonmania.entities.logicals.logics;

import dungeonmania.entities.Switch;
import dungeonmania.entities.logicals.CurrentSubject;

public class OnlySwitch extends Logic {
    @Override
    public void updateAdjacentConductorsAttachment(CurrentSubject subject, boolean current) {
        if (subject instanceof Switch) {
            super.updateAdjacentConductorsAttachment(subject, current);
        }
    }

    @Override
    public void updateAdjacentConductorsDetachment(CurrentSubject subject, boolean current) {
        if (subject instanceof Switch) {
            super.updateAdjacentConductorsDetachment(subject, current);
        }
    }

    @Override
    public void updateNumActivated(CurrentSubject subject, boolean current) {
        if (subject instanceof Switch) {
            super.updateNumActivated(subject, current);
        }
    }

    @Override
    public boolean checkIfFulfilled() {
        return getNumActivated() > 0;
    }

}
