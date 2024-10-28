package dungeonmania.goals;

import dungeonmania.Game;

public class TreasureGoal extends GoalWithTarget {
    public TreasureGoal(int target) {
        super(target);
    }

    @Override
    public boolean achievedIfPlayer(Game game) {
        return game.getCollectedTreasureCount() >= getTarget();
    }

    @Override
    public String toStringIfUnachieved(Game game) {
        return ":treasure";
    }
}
