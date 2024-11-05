package dungeonmania.entities.logicals.logics;

public class LogicFactory {
    public static Logic getLogic(String logic) {
        switch (logic) {
        case "or":
            return new Or();
        case "and":
            return new And();
        case "xor":
            return new Xor();
        case "co_and":
            return new CoAnd();
        case "only_switch":
            return new OnlySwitch();
        default:
            return null;
        }
    }
}
