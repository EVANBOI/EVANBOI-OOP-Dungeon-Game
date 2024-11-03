// package dungeonmania.mvp.task2;

// import dungeonmania.DungeonManiaController;
// import dungeonmania.exceptions.InvalidActionException;
// import dungeonmania.mvp.TestUtils;
// import dungeonmania.response.models.DungeonResponse;
// import dungeonmania.util.Direction;
// import dungeonmania.util.Position;

// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;

// import static org.junit.jupiter.api.Assertions.*;

// public class LogicSwitchesTest {
//     @Test
//     @DisplayName("Test logical entity activation by cardinally adjacent wire")
//     public void wireLightBulbANDActivation() {
//         DungeonManiaController dmc;
//         dmc = new DungeonManiaController();
//         DungeonResponse res = dmc.newGame("d_logicSwitchesTest_wireLightBulbANDActivation", "c_logicSwitchesTest");

//         // assert there is one light bulb that is off at initial state
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 1);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 0);

//         // activate switch and hence the adjacent wires
//         res = dmc.tick(Direction.RIGHT);

//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 0);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 1);
//     }

//     @Test
//     @DisplayName("Test logical entity activation by cardinally adjacent switch")
//     public void switchLightBulbORActivation() {
//         DungeonManiaController dmc;
//         dmc = new DungeonManiaController();
//         DungeonResponse res = dmc.newGame("d_logicSwitchesTest_switchLightBulbORActivation", "c_logicSwitchesTest");

//         // assert there is one light bulb that is off at initial state
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 1);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 0);

//         // activate switch
//         res = dmc.tick(Direction.RIGHT);

//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 0);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 1);
//     }

//     @Test
//     @DisplayName("Test OR logical entity activation")
//     public void orActivation() {
//         DungeonManiaController dmc;
//         dmc = new DungeonManiaController();
//         DungeonResponse res = dmc.newGame("d_logicSwitchesTest_orActivation", "c_logicSwitchesTest");

//         // assert there is one light bulb that is off at initial state
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 1);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 0);

//         // activate switch and hence 1 of 4 wires around the light bulb
//         res = dmc.tick(Direction.RIGHT);

//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 0);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 1);
//     }

//     @Test
//     @DisplayName("Test AND logical entity activation")
//     public void andActivation() {
//         DungeonManiaController dmc;
//         dmc = new DungeonManiaController();
//         DungeonResponse res = dmc.newGame("d_logicSwitchesTest_andActivation", "c_logicSwitchesTest");

//         // assert there is one light bulb that is off at initial state
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 1);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 0);

//         // activate switch and hence 1 of the 2 wires cardinally adjacent to light bulb
//         res = dmc.tick(Direction.UP);
//         res = dmc.tick(Direction.RIGHT);

//         // assert light bulb still not on
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 1);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 0);

//         res = dmc.tick(Direction.LEFT);
//         res = dmc.tick(Direction.DOWN);
//         res = dmc.tick(Direction.DOWN);
//         res = dmc.tick(Direction.RIGHT);

//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 0);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 1);
//     }

//     @Test
//     @DisplayName("Test XOR logical entity activation")
//     public void xorActivation() {
//         DungeonManiaController dmc;
//         dmc = new DungeonManiaController();
//         DungeonResponse res = dmc.newGame("d_logicSwitchesTest_andActivation", "c_logicSwitchesTest");

//         // assert there is one light bulb that is off at initial state
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 1);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 0);

//         // activate switch and hence 1 of the 2 wires cardinally adjacent to light bulb
//         res = dmc.tick(Direction.UP);
//         res = dmc.tick(Direction.RIGHT);

//         // assert light bulb is on
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 0);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 1);

//         // activate other switch resulting in 2 adjacent activated conductors
//         res = dmc.tick(Direction.LEFT);
//         res = dmc.tick(Direction.DOWN);
//         res = dmc.tick(Direction.DOWN);
//         res = dmc.tick(Direction.RIGHT);

//         // assert light bulb is off
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 1);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 0);

//         // turn a switch back off
//         res = dmc.tick(Direction.DOWN);
//         res = dmc.tick(Direction.RIGHT);
//         res = dmc.tick(Direction.UP);

//         // assert light bulb is on
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 0);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 1);
//     }

//     @Test
//     @DisplayName("Test CO_AND logical entity activation 'refreshed' behaviour")
//     public void coAndActivationRefresh() {
//         DungeonManiaController dmc;
//         dmc = new DungeonManiaController();
//         DungeonResponse res = dmc.newGame("d_logicSwitchesTest_coAndActivation", "c_logicSwitchesTest");

//         // assert there is one light bulb that is off at initial state
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 1);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 0);

//         // activate switch and hence 1 of the 2 wires cardinally adjacent to light bulb
//         res = dmc.tick(Direction.LEFT);
//         res = dmc.tick(Direction.LEFT);
//         res = dmc.tick(Direction.UP);
//         res = dmc.tick(Direction.RIGHT);

//         // assert light bulb is off
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 1);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 0);

//         // activate other switch resulting in 2 adjacent activated conductors on different ticks
//         res = dmc.tick(Direction.LEFT);
//         res = dmc.tick(Direction.DOWN);
//         res = dmc.tick(Direction.DOWN);
//         res = dmc.tick(Direction.RIGHT);

//         // assert light bulb is off
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 1);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 0);

//         // check that current is not refreshed
//         res = dmc.tick(Direction.UP);
//         res = dmc.tick(Direction.RIGHT);
//         res = dmc.tick(Direction.RIGHT);

//         // assert light bulb is off
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 1);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 0);
//     }

//     @Test
//     @DisplayName("Test CO_AND logical entity activation")
//     public void coAndActivation() {
//         DungeonManiaController dmc;
//         dmc = new DungeonManiaController();
//         DungeonResponse res = dmc.newGame("d_logicSwitchesTest_coAndActivation", "c_logicSwitchesTest");

//         // assert there is one light bulb that is off at initial state
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 1);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 0);

//         // activate switch resulting in all adjacent conductors to light bulb activating
//         res = dmc.tick(Direction.RIGHT);

//         // assert light bulb is on
//         assertEquals(TestUtils.countType(res, "light_bulb_off"), 0);
//         assertEquals(TestUtils.countType(res, "light_bulb_on"), 1);
//     }

//     @Test
//     @DisplayName("Test logical bomb")
//     public void logicalBomb() throws InvalidActionException {
//         DungeonManiaController dmc;
//         dmc = new DungeonManiaController();
//         DungeonResponse res = dmc.newGame("d_logicSwitchesTest_logicalBomb", "c_logicSwitchesTest");

//         assertEquals(1, TestUtils.getEntities(res, "bomb").size());
//         assertEquals(2, TestUtils.getEntities(res, "boulder").size());
//         assertEquals(2, TestUtils.getEntities(res, "switch").size());
//         assertEquals(1, TestUtils.getEntities(res, "wall").size());
//         assertEquals(1, TestUtils.getEntities(res, "treasure").size());
//         assertEquals(3, TestUtils.getEntities(res, "wire").size());

//         // pick up bomb
//         res = dmc.tick(Direction.DOWN);

//         // place bomb
//         res = dmc.tick(Direction.RIGHT);
//         res = dmc.tick(Direction.RIGHT);
//         res = dmc.tick(Direction.RIGHT);
//         res = dmc.tick(Direction.RIGHT);
//         res = dmc.tick(Direction.RIGHT);
//         res = dmc.tick(Direction.RIGHT);
//         res = dmc.tick(Direction.UP);
//         res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());

//         // Check bomb did not explode
//         assertEquals(1, TestUtils.getEntities(res, "bomb").size());
//         assertEquals(2, TestUtils.getEntities(res, "boulder").size());
//         assertEquals(2, TestUtils.getEntities(res, "switch").size());
//         assertEquals(1, TestUtils.getEntities(res, "wall").size());
//         assertEquals(1, TestUtils.getEntities(res, "treasure").size());
//         assertEquals(3, TestUtils.getEntities(res, "wire").size());

//         // activate switch resulting in all adjacent conductors to light bulb activating
//         res = dmc.tick(Direction.DOWN);
//         res = dmc.tick(Direction.LEFT);
//         res = dmc.tick(Direction.LEFT);
//         res = dmc.tick(Direction.LEFT);
//         res = dmc.tick(Direction.LEFT);
//         res = dmc.tick(Direction.LEFT);
//         res = dmc.tick(Direction.LEFT);
//         res = dmc.tick(Direction.UP);
//         res = dmc.tick(Direction.RIGHT);

//         // assert bomb exploded
//         assertEquals(0, TestUtils.getEntities(res, "bomb").size());
//         assertEquals(1, TestUtils.getEntities(res, "boulder").size());
//         assertEquals(1, TestUtils.getEntities(res, "switch").size());
//         assertEquals(0, TestUtils.getEntities(res, "wall").size());
//         assertEquals(0, TestUtils.getEntities(res, "treasure").size());
//         assertEquals(2, TestUtils.getEntities(res, "wire").size());
//     }

//     @Test
//     @DisplayName("Test player can not walk through switch door")
//     public void cannotWalkThroughSwitchDoor() {
//         DungeonManiaController dmc;
//         dmc = new DungeonManiaController();
//         DungeonResponse res = dmc.newGame("d_logicSwitchesTest_switchDoor", "c_logicSwitchesTest");

//         Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

//         // try to walk through door and fail
//         res = dmc.tick(Direction.RIGHT);
//         assertEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
//     }

//     @Test
//     @DisplayName("Test activation of switch door")
//     public void switchDoor() {
//         DungeonManiaController dmc;
//         dmc = new DungeonManiaController();
//         DungeonResponse res = dmc.newGame("d_logicSwitchesTest_switchDoor", "c_logicSwitchesTest");

//         // activate switch door
//         res = dmc.tick(Direction.DOWN);

//         res = dmc.tick(Direction.UP);
//         Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
//         // try to walk through door and succeed
//         res = dmc.tick(Direction.RIGHT);
//         assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
//     }
// }
