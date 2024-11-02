package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class EnemyGoal extends GoalWithTarget {
    public EnemyGoal(int target) {
        super(target);
    }

    @Override
    public boolean achievedIfPlayer(Game game) {
        Player character = game.getPlayer();
        if (character.getDefeatedEnemiesCount() < getTarget()) {
            return false;
        }
        if (game.getMap().getEntities(ZombieToastSpawner.class).stream().count() != 0) {
            return false;
        }
        return true;
    }

    @Override
    public String toStringIfUnachieved(Game game) {
        return ":enemies";
    }

}
