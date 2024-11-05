package dungeonmania.entities.logicals.logics;

public class Xor extends Logic {
    @Override
    public boolean checkIfFulfilled() {
        return getNumActivated() == 1;
    }
}
