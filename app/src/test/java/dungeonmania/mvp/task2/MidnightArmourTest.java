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

public class MidnightArmourTest {
    @Test
    @DisplayName("Test midnight armour being built around zombies")
    public void buildMidnightArmourAroundZombies() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_midnightArmourTest_buildMidnightArmourAroundZombies",
                "c_midnightArmourTest_buildMidnightArmourAroundZombies");
        // Collect sword
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "sword").size());
        // Collect sun stone
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "sun_stone").size());
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
    };

    @Test
    @DisplayName("Test midnight armour being built successfully")
    public void buildMidnightArmour() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_midnightArmourTest_buildMidnightArmour",
                "c_midnightArmourTest_buildMidnightArmour");
        // Collect sword
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "sword").size());
        // Collect sun stone
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "sun_stone").size());
        // Build midnight armour
        response = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(response, "midnight_armour").size());

    };

    @Test
    @DisplayName("Test midnight armour buff")
    public void midnightArmourBuff() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_midnightArmourTest_midnightArmourBuff",
                "c_midnightArmourTest_midnightArmourBuff");

        // Get all entities
        List<EntityResponse> entities = response.getEntities();

        // Collect sword
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "sword").size());
        // Collect sun stone
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "sun_stone").size());
        // Build midnight armour
        response = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(response, "midnight_armour").size());

        // Fight Spider
        response = dmc.tick(Direction.RIGHT);
        response = dmc.tick(Direction.RIGHT);
        response = dmc.tick(Direction.RIGHT);
        response = dmc.tick(Direction.RIGHT);

        response = dmc.tick(Direction.DOWN);
        response = dmc.tick(Direction.DOWN);
        response = dmc.tick(Direction.DOWN);

        entities = response.getEntities();
        assertTrue(TestUtils.countEntityOfType(entities, "spider") == 0);

    };

}
