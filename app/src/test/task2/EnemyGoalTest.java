package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class EnemyGoalTest {
    @Test
    @DisplayName("Test achieving a basic enemies goal with 1 target")
    public abstract void oneEnemyTarget();

    @Test
    @DisplayName("Test achieving a basic enemies goal with 1 target and 1 spawner")
    public abstract void oneEnemyTargetAndSpawner();

    @Test
    @DisplayName("Test achieving a basic enemies goal with non single enemy target")
    public abstract void threeEnemyTarget();

    @Test
    @DisplayName("Testing a map with conjunction goals including enemy goal")
    public abstract void enemyAndTreasureAndSwitch();

    @Test
    @DisplayName("Testing that the exit goal must be achieved last in EXIT and ENEMIES")
    public abstract void exitAndTreasureOrder();
}
