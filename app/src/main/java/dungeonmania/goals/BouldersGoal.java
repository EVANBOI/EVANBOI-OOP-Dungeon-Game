package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.Switch;

public class BouldersGoal extends Goal {
    @Override
    public boolean achievedIfPlayer(Game game) {
        return game.getMap().getEntities(Switch.class).stream().allMatch(s -> s.isActivated());
    }

    @Override
    public String toStringIfUnachieved(Game game) {
        return ":boulders";
    }
}
