package dungeonmania.entities.logicals.logics;

public class Or extends Logic {
    @Override
    public boolean checkIfFulfilled() {
        return getNumActivated() > 0;
    }
}
