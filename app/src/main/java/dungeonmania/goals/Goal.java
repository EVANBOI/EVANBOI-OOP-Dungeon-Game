package dungeonmania.goals;

import dungeonmania.Game;

public abstract class Goal {
    public abstract boolean achievedIfPlayer(Game game);

    public boolean achieved(Game game) {
        if (game.getPlayer() == null) {
            return false;
        } else {
            return achievedIfPlayer(game);
        }
    }

    public abstract String toStringIfUnachieved(Game game);

    public String toString(Game game) {
        if (this.achieved(game)) {
            return "";
        } else {
            return toStringIfUnachieved(game);
        }
    }
}
