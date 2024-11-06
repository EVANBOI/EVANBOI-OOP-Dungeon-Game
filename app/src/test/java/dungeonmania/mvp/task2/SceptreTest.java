package dungeonmania.mvp.task2;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

public class SceptreTest {
    @Test
    @DisplayName("Test sceptre on mercenary and sceptre duration and durability")
    public void sceptreOnMercenary() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_sceptreTest_sceptreOnMercenary", "c_sceptreTest_sceptreOnMercenary");
        String mercenaryId = TestUtils.getEntitiesStream(response, "mercenary").findFirst().get().getId();

        // Check that you cannot bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercenaryId));

        // Pick up treasure
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "treasure").size());

        // Pick up wood
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "wood").size());

        // Pick up sun stone
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "sun_stone").size());

        // Build sceptre
        response = assertDoesNotThrow(() -> dmc.build("sceptre"));

        // Use sceptre
        response = assertDoesNotThrow(() -> dmc.interact(mercenaryId));

        // Check that the mercenary is under mind control
        response = dmc.tick(Direction.DOWN);
        response = dmc.tick(Direction.DOWN);
        response = dmc.tick(Direction.DOWN);
        response = dmc.tick(Direction.DOWN);
        // The mercenary is now an ally and follows the player
        response = dmc.tick(Direction.DOWN);
        response = dmc.tick(Direction.UP);
        List<EntityResponse> entities = response.getEntities();
        assertTrue(TestUtils.countEntityOfType(entities, "mercenary") == 1);
        response = dmc.tick(Direction.DOWN);
        response = dmc.tick(Direction.UP);
        response = dmc.tick(Direction.DOWN);
        // Check that Mercenary is not under mind control after this tick
        response = dmc.tick(Direction.UP);
        entities = response.getEntities();
        assertTrue(TestUtils.countEntityOfType(entities, "mercenary") == 0);

    };

    @Test
    @DisplayName("Test building sceptre using arrows")
    public void buildSceptreUsingArrows() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_sceptreTest_buildSceptreUsingArrows",
                "c_sceptreTest_buildSceptreUsingArrows");

        // Pick up arrow x2
        response = dmc.tick(Direction.DOWN);
        response = dmc.tick(Direction.DOWN);
        assertEquals(2, TestUtils.getInventory(response, "arrow").size());

        // Pick up key
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "key").size());

        // Pick up sun stone
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "sun_stone").size());

        // Build Sceptre
        response = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(response, "sceptre").size());
    };

    @Test
    @DisplayName("Test building sceptre using 2 sun stones")
    public void buildSceptreUsingSunStones() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_sceptreTest_buildSceptreUsingSunStones",
                "c_sceptreTest_buildSceptreUsingSunStones");

        // Pick up arrow x2
        response = dmc.tick(Direction.DOWN);
        response = dmc.tick(Direction.DOWN);
        assertEquals(2, TestUtils.getInventory(response, "arrow").size());

        // Pick up sun stone x2
        response = dmc.tick(Direction.DOWN);
        response = dmc.tick(Direction.DOWN);
        assertEquals(2, TestUtils.getInventory(response, "sun_stone").size());

        // Build Sceptre
        response = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(response, "sceptre").size());

        // One sun stone gets consumed
        assertEquals(1, TestUtils.getInventory(response, "sun_stone").size());
    };
}
