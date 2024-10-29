package dungeonmania.goals;

import dungeonmania.Game;

public abstract class GoalsOperand extends Goal {
    private Goal goal1;
    private Goal goal2;

    public GoalsOperand(Goal goal1, Goal goal2) {
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    public Goal getGoal1() {
        return goal1;
    }

    public Goal getGoal2() {
        return goal2;
    }

    public abstract boolean achievedIfPlayer(Game game);

    public abstract String toStringIfUnachieved(Game game);
}
