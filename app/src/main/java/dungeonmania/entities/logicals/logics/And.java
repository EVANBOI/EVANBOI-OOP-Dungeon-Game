package dungeonmania.entities.logicals.logics;

public class And extends Logic {
    @Override
    public boolean checkIfFulfilled() {
        return (getNumActivated() == getTotalValidAdjacentConductors() & getTotalValidAdjacentConductors() > 1);
    }
}
