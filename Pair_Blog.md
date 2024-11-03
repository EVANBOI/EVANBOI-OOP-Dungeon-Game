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

### Choice 1 (Insert choice)

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

### Choice 2 (Insert choice)

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
