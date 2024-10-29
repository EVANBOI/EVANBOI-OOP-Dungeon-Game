package dungeonmania.goals;

import dungeonmania.Game;

public abstract class GoalWithTarget extends Goal {
    private int target;

    public GoalWithTarget(int target) {
        this.target = target;
    }

    public int getTarget() {
        return target;
    }

    public abstract boolean achievedIfPlayer(Game game);

    public abstract String toStringIfUnachieved(Game game);
}
