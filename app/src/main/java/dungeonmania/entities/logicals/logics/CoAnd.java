package dungeonmania.entities.logicals.logics;

public class CoAnd extends Logic {
    private boolean conditionAlreadyMet = false;
    private int numActivatedLastTick = 0;

    @Override
    public boolean checkIfFulfilled() {
        boolean condition;
        if (!conditionAlreadyMet) {
            condition = (numActivatedLastTick == 0 & getNumActivated() == getTotalValidAdjacentConductors());
        } else {
            condition = (getNumActivated() == getTotalValidAdjacentConductors());
        }
        numActivatedLastTick = getNumActivated();
        conditionAlreadyMet = condition;
        return condition;
    }
}
