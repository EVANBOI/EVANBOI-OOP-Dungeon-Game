package dungeonmania.entities.logicals.logics;

import dungeonmania.entities.logicals.CurrentSubject;

public abstract class Logic {
    private int numActivated = 0;
    private int totalValidAdjacentConductors = 0;

    public void updateAdjacentConductorsAttachment(CurrentSubject subject, boolean current) {
        if (current) {
            incrementNumActivated(true);
        }
        incrementTotalValidAdjacentConductors(true);
    }

    public void updateAdjacentConductorsDetachment(CurrentSubject subject, boolean current) {
        if (current) {
            incrementNumActivated(false);
        }
        incrementTotalValidAdjacentConductors(false);
    }

    public void updateNumActivated(CurrentSubject subject, boolean current) {
        incrementNumActivated(current);
    }

    public void incrementNumActivated(boolean up) {
        if (up) {
            numActivated++;
        } else {
            numActivated--;
        }
    }

    public void incrementTotalValidAdjacentConductors(boolean up) {
        if (up) {
            totalValidAdjacentConductors++;
        } else {
            totalValidAdjacentConductors--;
        }
    }

    public int getNumActivated() {
        return numActivated;
    }

    public int getTotalValidAdjacentConductors() {
        return totalValidAdjacentConductors;
    }

    public abstract boolean checkIfFulfilled();
}
