package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.OnDestroyBehaviour;
import dungeonmania.entities.OverlapBehaviour;
import dungeonmania.entities.Player;
import dungeonmania.entities.PotionListener;
import dungeonmania.entities.enemies.movement.EnemyMovement;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Enemy extends Entity implements Battleable, OverlapBehaviour, OnDestroyBehaviour {
    private BattleStatistics battleStatistics;
    private EnemyMovement movement;

    public Enemy(Position position, double health, double attack) {
        super(position.asLayer(Entity.CHARACTER_LAYER));
        battleStatistics = new BattleStatistics(health, attack, 0, BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER);
    }

    public void setMovement(EnemyMovement movement) {
        this.movement = movement;
    }

    public EnemyMovement getMovement() {
        return movement;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Player;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            map.getGame().battle(player, this);
        }
    }

    @Override
    public void onDestroy(GameMap map) {
        Game g = map.getGame();
        g.unsubscribe(getId());
        if (this instanceof PotionListener)
            map.getPlayer().removePotionListener((PotionListener) this);
    }

    public abstract void move(Game game);
}
