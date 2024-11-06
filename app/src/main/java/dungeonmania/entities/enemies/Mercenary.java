package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.PotionListener;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.movement.RandomMovement;
import dungeonmania.entities.enemies.movement.RunAwayMovement;
import dungeonmania.entities.enemies.movement.ToPlayerMovement;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable, PotionListener {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;

    private double allyAttack;
    private double allyDefence;
    private boolean allied = false;
    private boolean isAdjacentToPlayer = false;
    private int mindControlDuration = 0;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
        setMovement(new ToPlayerMovement());
    }

    public boolean isAllied() {
        if (mindControlDuration > 0) {
            return true;
        }
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied)
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        return bribeRadius >= 0 && player.countEntityOfType(Treasure.class) >= bribeAmount;
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }
    }

    @Override
    public void interact(Player player, Game game) {

        if (player.getSceptre() != null) {
            sceptreListener(game);
        } else {
            allied = true;
            setMovement(new ToPlayerMovement());
            bribe(player);
            if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), getPosition()))
                isAdjacentToPlayer = true;
        }
    }

    @Override
    public void move(Game game) {
        GameMap map = game.getMap();
        Position nextPos = getMovement().getNextEnemyPos(map, this);
        Player player = game.getPlayer();

        if (allied) {
            nextPos = isAdjacentToPlayer ? player.getPreviousDistinctPosition() : nextPos;
            if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), nextPos))
                isAdjacentToPlayer = true;
        }

        map.moveTo(this, nextPos);

        if (mindControlDuration > 0) {
            mindControlDuration--;
            if (mindControlDuration == 0) {
                allied = false;
            }
        }
    }

    @Override
    public boolean isInteractable(Player player) {
        if (!allied && canBeBribed(player)) {
            return true;
        } else if (player.getSceptre() != null) {
            return true;
        }
        return false;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        if (!allied && mindControlDuration == 0)
            return super.getBattleStatistics();
        return new BattleStatistics(0, allyAttack, allyDefence, 1, 1);
    }

    @Override
    public void notifyPotion(Potion potion) {
        if (allied)
            return;

        if (potion instanceof InvisibilityPotion)
            setMovement(new RandomMovement());
        if (potion instanceof InvincibilityPotion)
            setMovement(new RunAwayMovement());
    }

    @Override
    public void notifyNoPotion() {
        if (allied)
            return;

        setMovement(new ToPlayerMovement());
    }

    public void sceptreListener(Game game) {
        Player player = game.getPlayer();
        Sceptre sceptre = player.getSceptre();
        mindControlDuration = sceptre.getDuration();
        allied = true;
    }

}
