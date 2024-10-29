package dungeonmania.goals;

import dungeonmania.Game;

public class And extends GoalsOperand {
    public And(Goal goal1, Goal goal2) {
        super(goal1, goal2);
    }

    @Override
    public boolean achievedIfPlayer(Game game) {
        return getGoal1().achieved(game) && getGoal2().achieved(game);
    }

    @Override
    public String toStringIfUnachieved(Game game) {
        return "(" + getGoal1().toString(game) + " AND " + getGoal2().toString(game) + ")";
    }
}
