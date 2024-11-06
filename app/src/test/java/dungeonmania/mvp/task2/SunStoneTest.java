package dungeonmania.mvp.task2;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class SunStoneTest {
    @Test
    @DisplayName("Test sun stone opening door")
    public void testSunStoneOpenDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_sunStoneTest_testSunStoneOpenDoor", "c_basicGoalsTest_exit");

        // Pick up sun stone
        response = dmc.tick(Direction.DOWN);
        // Check that we have sun Stone
        assertEquals(1, TestUtils.getInventory(response, "sun_stone").size());

        // Walk through first door
        response = dmc.tick(Direction.DOWN);
        response = dmc.tick(Direction.DOWN);
        response = dmc.tick(Direction.DOWN);

        // Check that we still have sun stone
        assertEquals(1, TestUtils.getInventory(response, "sun_stone").size());

    };

    @Test
    @DisplayName("Test sun stone can build shield")
    public void testSunStoneBuildShield() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_sunStoneTest_testSunStoneBuildShield",
                "c_sunStoneTest_testSunStoneOpenDoor");
        // Start with empty inventory
        assertEquals(0, TestUtils.getInventory(response, "wood").size());
        assertEquals(0, TestUtils.getInventory(response, "sun_stone").size());

        // Pick up wood x2
        response = dmc.tick(Direction.RIGHT);
        response = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(response, "wood").size());

        // Pick up sun stone
        response = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(response, "sun_stone").size());

        // Build Shield
        assertEquals(0, TestUtils.getInventory(response, "shield").size());
        response = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(response, "shield").size());

        // Sun stone isn't used
        assertEquals(0, TestUtils.getInventory(response, "wood").size());
        assertEquals(1, TestUtils.getInventory(response, "sun_stone").size());

    };

    @Test
    @DisplayName("Test that keys/treasure are used to build shield")
    public void testSunStoneCraftingPriority() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_sunStoneTest_testSunStoneCraftingPriority",
                "c_sunStoneTest_testSunStoneCraftingPriority");
        // Start with empty inventory
        assertEquals(0, TestUtils.getInventory(response, "wood").size());
        assertEquals(0, TestUtils.getInventory(response, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(response, "key").size());

        // Pick up wood x2
        response = dmc.tick(Direction.RIGHT);
        response = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(response, "wood").size());

        // Pick up sun stone
        response = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(response, "sun_stone").size());

        // Pick up key
        response = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(response, "key").size());

        // Build Shield
        assertEquals(0, TestUtils.getInventory(response, "shield").size());
        response = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(response, "shield").size());

        // Sun stone isn't used and key is used
        assertEquals(0, TestUtils.getInventory(response, "wood").size());
        assertEquals(1, TestUtils.getInventory(response, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(response, "key").size());
    };

    @Test
    @DisplayName("Test sun stone cannot bribe mercenary")
    public void testSunStoneBribeMercenary() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_sunStoneTest_testSunStoneBribeMercenary",
                "c_sunStoneTest_testSunStoneBribeMercenary");

        // Check that we don't have sun stone
        assertEquals(0, TestUtils.getInventory(response, "sun_stone").size());
        // Check that we can't bribe yet
        String mercId = TestUtils.getEntitiesStream(response, "mercenary").findFirst().get().getId();
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));

        // Pick up sun stone
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "sun_stone").size());
        // Check that we can't bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));

        // Pick up treasure
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "treasure").size());
        // Check that we can bribe
        response = assertDoesNotThrow(() -> dmc.interact(mercId));

        // Check that we still have sun stone
        assertEquals(1, TestUtils.getInventory(response, "sun_stone").size());

    };

    @Test
    @DisplayName("Test sun stone counts towards treasure goal")
    public void testSunStoneTreasureGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_sunStoneTest_testSunStoneTreasureGoal",
                "c_sunStoneTest_testSunStoneTreasureGoal");
        // No treasure has been collected
        int numTreasuresTotal = 0;

        // collect sun stone
        response = dmc.tick(Direction.RIGHT);
        numTreasuresTotal = TestUtils.getInventory(response, "sun_stone").size()
                + TestUtils.getInventory(response, "treasure").size();
        assertEquals(1, numTreasuresTotal);

        // goal has not been met
        assertTrue(TestUtils.getGoals(response).contains(":treasure"));

        // collect treasure
        response = dmc.tick(Direction.RIGHT);
        numTreasuresTotal = TestUtils.getInventory(response, "sun_stone").size()
                + TestUtils.getInventory(response, "treasure").size();
        assertEquals(2, numTreasuresTotal);

        // goal has not been met
        assertTrue(TestUtils.getGoals(response).contains(":treasure"));

        // collect treasure
        response = dmc.tick(Direction.RIGHT);

        // goal has been met
        assertEquals("", TestUtils.getGoals(response));

    };
}
