package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public abstract class PlayerState {
    private Player player;

    PlayerState(Player player) {
        this.player = player;
    }

    public boolean isInvincible() {
        return false;
    }

    public boolean isInvisible() {
        return false;
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        return origin;
    }

    public void transitionInvisible() {
        player.changeState(new InvisibleState(player));
    }

    public void transitionInvincible() {
        player.changeState(new InvincibleState(player));
    }

    public void transitionBase() {
        player.changeState(new BaseState(player));
    }
}
