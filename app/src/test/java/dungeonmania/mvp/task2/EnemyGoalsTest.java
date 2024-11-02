package dungeonmania.mvp.task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyGoalsTest {
    @Test
    @DisplayName("Test achieving a basic enemies goal with 1 target")
    public void oneEnemyTarget() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalsTest_oneEnemyTarget", "c_enemyGoalsTest_oneEnemyTarget");
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // kills spider
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.getGoals(res), "");
    }

    @Test
    @DisplayName("Test achieving a basic enemies goal with 1 target and 1 spawner")
    public void oneEnemyTargetAndSpawner() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalsTest_oneEnemyTargetAndSpawner",
                "c_enemyGoalsTest_oneEnemyTargetAndSpawner");

        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertEquals(TestUtils.countType(res, "spider"), 1);
        assertEquals(TestUtils.countType(res, "zombie_toast_spawner"), 1);
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // picks up sword
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // kills spider
        res = dmc.tick(Direction.LEFT);
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertEquals(TestUtils.countType(res, "spider"), 0);
        assertEquals(TestUtils.countType(res, "zombie_toast_spawner"), 1);

        // destroy spawner
        res = dmc.tick(Direction.UP);
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(TestUtils.countType(res, "zombie_toast_spawner"), 0);
        assertEquals(TestUtils.getGoals(res), "");
    }

    @Test
    @DisplayName("Test achieving a basic enemies goal with non single enemy target")
    public void threeEnemyTarget() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalsTest_threeEnemyTarget", "c_enemyGoalsTest_threeEnemyTarget");

        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertEquals(TestUtils.countType(res, "mercenary"), 3);

        // kill first enemy
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.countType(res, "mercenary"), 2);
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // kill second enemy
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.countType(res, "mercenary"), 1);
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // kill third enemy
        res = dmc.tick(Direction.RIGHT);
        assertEquals(TestUtils.countType(res, "mercenary"), 0);
        assertEquals(TestUtils.getGoals(res), "");
    }

    @Test
    @DisplayName("Testing a map with conjunction goals including enemy goal")
    public void enemyAndTreasureAndSwitch() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalsTest_enemyAndTreasureAndSwitch",
                "c_enemyGoalsTest_enemyAndTreasureAndSwitch");

        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // kill spider
        res = dmc.tick(Direction.RIGHT);
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move boulder onto switch
        res = dmc.tick(Direction.RIGHT);
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertFalse(TestUtils.getGoals(res).contains(":boulders"));

        // pickup treasure
        res = dmc.tick(Direction.DOWN);
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
        assertFalse(TestUtils.getGoals(res).contains(":treasure"));
        assertFalse(TestUtils.getGoals(res).contains(":boulders"));

        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Testing that the exit goal must be achieved last in EXIT and ENEMIES")
    public void exitAndEnemyOrder() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalsTest_exitAndEnemyOrder", "c_enemyGoalsTest_exitAndEnemyOrder");

        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertEquals(TestUtils.countType(res, "mercenary"), 1);

        // move onto exit
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertEquals(TestUtils.countType(res, "mercenary"), 1);

        // move off exit and kill mercenary
        res = dmc.tick(Direction.RIGHT);
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertEquals(TestUtils.countType(res, "mercenary"), 0);

        // move back onto exit
        res = dmc.tick(Direction.LEFT);
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
        assertFalse(TestUtils.getGoals(res).contains(":exit"));

        assertEquals("", TestUtils.getGoals(res));
    }
}
