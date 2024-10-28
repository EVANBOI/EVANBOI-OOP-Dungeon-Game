package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.PotionListener;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.movement.RandomMovement;
import dungeonmania.entities.enemies.movement.RunAwayMovement;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy implements PotionListener {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
        setMovement(new RandomMovement());
    }

    @Override
    public void move(Game game) {
        GameMap map = game.getMap();
        Position nextPos = getMovement().getNextEnemyPos(map, this);
        map.moveTo(this, nextPos);
    }

    @Override
    public void notifyPotion(Potion potion) {
        if (potion instanceof InvincibilityPotion)
            setMovement(new RunAwayMovement());
    }

    @Override
    public void notifyNoPotion() {
        setMovement(new RandomMovement());
    }

}
