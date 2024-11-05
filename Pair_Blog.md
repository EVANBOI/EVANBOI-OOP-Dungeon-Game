# Assignment II Pair Blog Template

## Task 1) Code Analysis and Refactoring ⛏️

### a) From DRY to Design Patterns

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T3/teams/H11A_ALBATROSS/assignment-ii/-/merge_requests/2)

> i. Look inside src/main/java/dungeonmania/entities/enemies. Where can you notice an instance of repeated code? Note down the particular offending lines/methods/fields.

[Answer]
The 'move' methods for the enemies use a switch statement to determine how an enemy moves. The way an enemy moves is the same between a Mercenary and ZombieToast, so it results in unnecessarily duplicated code. For example, both Mercenaries and ZombieToast run away when the Player is invincible. The 2 classes also use the same code for their random movement.

The Default health and Default attack also are repeated for all enemies, but I believe they are used as a static field for EntityFactory, and so they shouldn't be refactored out.

> ii. What Design Pattern could be used to improve the quality of the code and avoid repetition? Justify your choice by relating the scenario to the key characteristics of your chosen Design Pattern.

[Answer]
The aim of the design pattern should be to remove the duplicate code, and also manage the switch statements, and allow for more decoupling for when new enemies are added. The design pattern chosen should be suited for the scenario where an object needs to change its algorithm for moving at runtime. This narrows it down to either a Strategy or a State pattern. Since the State of the enemy i.e. hostile, player is invisible or invincible...., does not affect how the algorithm is changed, a Strategy pattern is more suitable. The Strategy pattern does this by encapsulating each of the movement algorithms, and can easily make them interchangeable for each Enemy.

> iii. Using your chosen Design Pattern, refactor the code to remove the repetition.

[Briefly explain what you did]
The code was refactored by introducing a Strategy design pattern. A new folder was created in 'enemies', called 'movement', which is a directory containing an interface called EnemyMovement, and 4 other movement algorithms that implement EnemyMovement. The EnemyMovement class has a single method called getNextEnemyPos.

A EnemyMovement field was added to the Enemy class, with a getter and setter method for it. For each of the enemy subclasses, whenever they are instantiated, the EnemyMovement field is set to the default movement algorithm for that subclass. Originally, whenever a player uses a potion which would change the enemy's behaviour, the code would change a string that was used in the switch statement. Now, it was changed so that a setter method is used to change the movement algorithm instead.

When the move method is called for each enemy subclass, the current movement algorithm is used to calculate the next position the enemy should be in, which is returned, and then moveTo is called within the subclass on the returned position.

While Spider didn't have any duplicate code, a algorithm for Spider movement was also refactored into the Strategy pattern to allow for reuse in the future for other entities that may move in a square shape and is blocked by boulders. This involved moving most of the logic in Spiders, such as the 3 attributes: movementTrajectory, nextPositionElement, forward, and the method updateNextPosition to the SpiderMovement class.

### b) Pattern Analysis

[Links to your merge requests](/put/links/here)

> i. Identify one place where the State Pattern is present in the codebase. Do you think this is an appropriate use of the State Pattern?
The State Pattern was used for transitioning between Player states, including their base state, invisible state and invincible state. Although the classes represent states, it was still an inappropriate use of the pattern. This is because the State pattern should be used when an algorithm changes behaviour depending on its State. This was not the case for the implementation, as the the code for each of the States was the same, resulting in a lot of unnecessarily duplicated code. The current implementation also violates open-closed principle, where any additional PlayerState would require the adding of a new boolean attribute to the PlayerState class.

[Answer]

> ii. (Option 1) If you answered that it was an appropriate use of the State Pattern, justify how the implementation relates to the key characteristics of the State Pattern.

> (Option 2) If you answered that it was not an appropriate use of the State Pattern, refactor the code to improve the implementation. You may choose to improve the usage of the pattern, switch to a different design pattern, or remove the pattern entirely.

[Answer or brief explanation of your code]
I refactored the code by improving the usage of the pattern. I removed all of the duplicate transition methods,and moved it to the PlayerState class to remove the duplicate code. I also removed the boolean isInvisible and isInvincible attributes from playerState, and instead made the methods in each type of State to either return true or false. To make use of the State pattern, I moved the applyBuff method from the Player class and is instead implemented in each of the states. Originally, the code would cycle through all of the states and check for the corresponding one, and then apply the buff. Now, the code uses the State pattern to choose the correct type of buff to apply.

### c) Inheritance Design

[Links to your merge requests](/put/links/here)

> i. List one design principle that is violated by collectable objects based on the description above. Briefly justify your answer.

[Answer]

Based on the previous engineering team’s feedback, we can identify a violation of the Liskov Substitution Principle (LSP). An LSP violation occurs when attributes or methods in a superclass don’t logically apply to all subclasses, making them incompatible with each other. In this case, it doesn’t make sense for Wood and Treasure classes to inherit attributes like durability or methods for applying buffs. Additionally, upon further inspection with Alex we discovered that the abstract method for onOverlap is present in Buildables, a subclass of InventoryItem. This violates LSP as items that are crafted like shields and swords are not on the map to overlap on. 

This further looks true if we look at the battleFacade class as by doing so we can see that swords, shields, potions and bows are the only items that are able to apply buffs on the player.

> ii. Refactor the inheritance structure of the code, and in the process remove the design principle violation you identified.

[Briefly explain what you did]

When trying to remove the LSP violation, I considered creating a new subclass of InventoryItem named ConsumableInventoryItem. The
class contains behaviour regarding durability and applying buffs. By treating certain collectable items as subclasses of InventoryItem and ConsumableInventoryItem, I am able to remove the LSP violation. 

I realised that the abstract method onOverlap violates LSP, as it doesn't make sense for buildable items to have onOverlap behaviour. To fix this, we at first considered delegation but decided against it as it is important that shield and bow are subclasses of InventoryItem. Due to this, we decided to implement a OverlapBehaviour interface. Overall with this, we are able to remove the LSP violation present in the collectables folder. 


### d) More Code Smells

[Links to your merge requests](/put/links/here)

> i. What code smell is present in the above snippet?

[Answer]
The code smell present in the snippet is feature envy. This is because the method is providing logic regarding bomb detonation. This behaviour should not be in the switch class, as it is a violation of single responsibility principle.Additionally, after
discussing with Alex, another code smell, inappropriate intimacy is present. This is because the when obtaining the X and Y coordinates of the bomb, there is a violation of Law of Demeter. 

> ii. Refactor the code to resolve the smell and underlying problem causing it.

To resolve the smell, I move the bomb detonation logic to the bomb class. Additionally, added two new methods that return the X and Y coordinates of an entity in the entity class. 

[Briefly explain what you did]

### e) Open-Closed Goals

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T3/teams/H11A_ALBATROSS/assignment-ii/-/merge_requests/3)

> i. Do you think the design is of good quality here? Do you think it complies with the open-closed principle? Do you think the design should be changed?

[Answer]
The design is of poor quality here as it violates open-closed principle. The code uses a switch statement to cycle through all of the possible different types of goals. This  violates OCP as the implementation of any type of new goal would require changes to the existing Goal class, which could introduce bugs or make the code very complex and difficult to read. The code also introduces many attributes that are sometimes unused depending on the type of goal, which violates Single Responsibility Principle, as the class may be responsible for too many different types of goals. The design should thus be changed to fix these issues.

> ii. If you think the design is sufficient as it is, justify your decision. If you think the answer is no, pick a suitable Design Pattern that would improve the quality of the code and refactor the code accordingly.

[Briefly explain what you did]
I think the design is very insufficient. A design pattern that could be used to improve the quality of the code is the Composite pattern, with a tiny sprinkle of the template pattern. The composite pattern can be used so that the toString and achieved nodes can be used for any type of goal. The leaf nodes are the goals including exit, treasure and boulder, while the composite nodes are the And and Or operands for complex goals. The different types of goals were also separated based on the attributes it requires, where we have 3 abstract classes - Goal, GoalWithTarget and GoalsOperand. The latter 2 are subclasses of Goal. For the actual types of goals, And and Or are subclasses of GoalsOperand, Exit and Boulder are subclasses of Goal, and Treasure is subclass of GoalWithTarget. Since the achieved and toString had conditionally if statements that checked if the game had a player and if the goal was already achieved, we would have to repeat this check for every single goal class. We can circumvent this by using a template design pattern, where we have a conditional method for achieved and toString, and use them as methods inside another method which first does the conditional check.

### f) Open Refactoring

[Merge Request 1](/put/links/here)

Most classes don't require the onMoved method which violates LSP. To fix this, we decided to make an onMovedAway interface which is implemented by the entities where onMoved has special behaviour.

[Merge Request 2](/put/links/here)

Entities has a onDestroy method that is implemented by its subclasses. However, enemies is the only subclass that uses this methods, while the other overriden methods are empty. This violates LSP. To fix this, we implement a onDestroyBehaviour interface that is only implemented by enemies and zombieToastSpawner for now. (It will also be used for logical entities in Task2F). The GameMap class destroyEntity method is also updated so that it checks if the entity is a instance of the interface before calling onDestroy.

[Merge Request 3](/put/links/here)

Changed the buildables class into an interface, as it is possible to have a buildable item that lasts forever and doesn't have a durability. It is also possible to have a buildable item that doesn't apply a buff. In addition to this, the buildables class didn't have sort of concrete methods so it might as well been an interface.

To reduce the repetition present in shield/bow/sword, I refactored the getDurability code and use code into the UseableBuffItem.java class. I also realised I can change the buffItem class into an interface.

## Task 2) Evolution of Requirements 👽

### a) Microevolution - Enemy Goal

[Links to your merge requests](/put/links/here)

**Assumptions**

- the number of enemies defeated only include those defeated in battle (no bombs)

**Design**

- Extending on the Composite pattern from Task 1, we can introduce a new class called EnemyGoal that is a subclass of GoalWithTarget
- We also add a new case "enemies" to the GoalFactory
- The EnemyGoal class overrides the achievedIfPlayer method and checks if the number of defeated enemies is sufficient for the target, and if the map has no more spawners
- To count the number of defeated enemies, we add a attribute called defeatedEnemyCount to the Player class 
    - this value is incremented whenever a battle leads to an enemy being killed
        - this would entail a incrementEnemiesDefeated method in Player, which is referenced in battle method of Game
- To check if the map has no more spawners, we can use the getEntities method that returns a list of entities of a specific class
    - ZombieToastSpawner.class should have a count of 0
- override the toStringIfUnachieved method to return ":enemies"


**Changes after review**

Partner review:
This looks immaculate!

No changes made.

**Test list**

- wrote function stubs in JUnit tests

**Other notes**

During the implementation and testing of this task, I found that there was a mistake in the MVP where ZombieToastSpawners weren't properly destroyed when a player interacted with it. This was fixed during this task as well by adding a line in ZombieToastSpawner that destroys the entity when it is interacted with. 

### Choice 1 (F - Logic Switches)

[Links to your merge requests](/put/links/here)

**Assumptions**

From the ed forum,
- destruction of switches and wires is undefined (so dont need to worry about it)
- assume switch_door has the same properties as a door when open and closed
- wires can be moved on


**Design**

- Specification breakdown:
    - Need to create 5 types of logical rules (one of them is the bombs original one)
        - Or
        - And
        - Xor
        - Co_and
        - OnlySwitch (for the original bomb)

        - This can be implemented using a strategy pattern, where each logical entity will be initialised with the declared logical rule
        - Create a abstract class: Logic
            - 1 method:
                - checkIfFulfilled
            - will have the above 5 subclasses

        (Post implementation hindsight edit):
            - this ended up being a bit more complicated and had to add methods:
                - for when bombs attach and detach or when a circuit is destroyed
                    - (This is undefined but it was a nice challenge)
                    - updateAdjacentConductorsAttachment
                    - updateAdjacentConductorsDetachment
                - and also:
                    - updateNumActivated
                    - incrementNumActivated
                    - incrmeentTotalValidAdjacentConductors
                    - getNumActivated
                    - getTotalValidAdjacentConductors

    - To check if there is power at each of the logic entities, we can use a observer pattern:
        - have an interface called CurrentSubject for the entities that can conduct i.e. wires and switches (subject)
            - methods:
                - attach();
                - detach();
                - notify:
        - have an interface for CurrentObserver for logical entities and also wires (observer)
            - methods:
                - update();

        (Post implementation hindsight edit):
            - a lot of the methods between different logical entities and conductors ended up being the same, so that subjects is now a subclass of Entity and observer is still an interface (due to Bomb being an InventoryItem subclass which will be a pain to refactor)

        - wires observe cardinally adjacent wires and switches
        - logical entities observe cardinally adjacent wires and switches

        - have an attribute numCardinallyAdjacentConductors and numActiveCardinallyAdjacentConductors (these values are updated when an observer notifies them)
        - numCardinallyAdjacentConductors is equal to the number of observers the entity has
        - numActiveCardinallyAdjacentConductors is updated when any of the entities it subscribes to become active
        - we can use these attributes for the logical rules
            - Or:
                - return true if numActiveCardinallyAdjacentConductors >= 1
            - And:
                - return true if numActiveCardinallyAdjacentConductors = numCardinallyAdjacentConductors and numCardinallyAdjacentConductors >= 2
            - Xor:
                - return true if numActiveCardinallyAdjacentConductors = 1
            - Co_and:
                - Have an attribute called numActiveCardinallyAdjacentConductorsLastTick
                - Have an boolean attribute storing if the logic was already satisfied last tick
                - update the boolean as true and return true if numActiveCardinallyAdjacentConductors = numCardinallyAdjacentConductors and numActiveCardinallyAdjacentConductorsLastTick = 0

                (Post implementation hindsight edit):
                    - This was the biggest problem during my implementation as i realised that the check had to be applied after all the conductors were updated during each tick as otherwise the last tick would be updated by 1 increment, making the logic never return true

            - OnlySwitch:
                - return true when the switch is activated (use the same logic as Or() but only attach to switches)
        - To ensure that the observer pattern doesn't create an infinite loop: i.e, a wire 1 notifies its adjacent wire 2 and then wire 2 notifies wire 1 and so on repeatedly
            - when a conductor notifies another entity, it also provides an id
            - the other entity only notifies entities that do not have the same id

    - Can create a method called like initialiseConductorObservers which would initialise all the observer relations in the GameBuilder class

    - Need to create 3 more subclasses of Entity: LightBulb, SwitchDoor, Wire, which will all be in the entities folder, and also edit the bomb class
        - these changes will be done by adding new string cases in EntityFactory
        - to add these entities to the frontend, we look at NameConverter where it converts the class name into a string
            - for light_bulb, we should have a method that returns a boolean indicating whether it is on or off

**Changes after review**

Looks good!

**Test list**

- wrote function stubs in JUnit tests

**Other notes**

[Any other notes]

### Choice 2 (D- Sun Stone & More Buildables)

[Links to your merge requests](/put/links/here)

**Assumptions**

<<<<<<< HEAD
- For creating a sceptre if the player doesn't possess enough treasure/keys, you would require two sun stones to build
the item, in which one gets consumed.
- The durability of the spectre is 1
- Only one sceptre can be used one at a time
- When the mind control duration is over --> mercenary movements become random
- Midnight Armor does not count as a weapon
=======
>>>>>>> main

**Design**

[Design]

- Specification Breakdown:
- Need to create three classes:
    - Sun Stone
        - can be used to open any door
            - change onOverlap logic in the door class
        - can be used interchangeably with treasures or keys when building entities
            - change the builditem logic in bow/shield class
        - can't be used to bribe mercenaries or assassins
        - considered as treasure
            - require me to change logic in pickup from the player class
    - Sceptre
        - crafted with (1 wood OR 2 arrows) and (1 key OR 1 treasure) + (1 sun stone)
        - mind control mercenaries/assassins to become allies
            - only usable when player interacts with the mercanary
            - interact behaviour similar to bribing
    - Midnight Armor
        - crafted with (1 sword + 1 sun stone) if there are no zombies in the dungeon
        - no durability
        - provides extra attack damage as well as protection


- Classes to consider:
    - Sun Stone --> subclass of inventory item
    - Sceptre --> subclass of buildable
    - Midnight Armor --> subclass of buildable

**Changes after review**

[Design review/Changes made]

**Test list**

[Test List]

**Other notes**

[Any other notes]

### Choice 3 (Insert choice) (If you have a 3rd member)

[Links to your merge requests](/put/links/here)

**Assumptions**

[Any assumptions made]

**Design**

[Design]

**Changes after review**

[Design review/Changes made]

**Test list**

[Test List]

**Other notes**

[Any other notes]

## Task 3) Investigation Task ⁉️

[Merge Request 1](/put/links/here)

[Briefly explain what you did]

[Merge Request 2](/put/links/here)

[Briefly explain what you did]

Add all other changes you made in the same format here:
