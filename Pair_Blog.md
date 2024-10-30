# Assignment II Pair Blog Template

## Task 1) Code Analysis and Refactoring ⛏️

### a) From DRY to Design Patterns

[Links to your merge requests](/put/links/here)

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

[Answer]

> ii. (Option 1) If you answered that it was an appropriate use of the State Pattern, justify how the implementation relates to the key characteristics of the State Pattern.

> (Option 2) If you answered that it was not an appropriate use of the State Pattern, refactor the code to improve the implementation. You may choose to improve the usage of the pattern, switch to a different design pattern, or remove the pattern entirely.

[Answer or brief explanation of your code]

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

I realised that the abstract method onOverlap violates LSP, as it doesn't make sense for buildable items to have onOverlap behaviour. To fix this, we at first considered delegation but decided against it as it is important that shield and bow are subclasses of InventoryItem. Due to this, we decided to implement a OverLappable interface. Overall with this, we are able to remove the LSP violation present in the collectables folder. 


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

[Links to your merge requests](/put/links/here)

> i. Do you think the design is of good quality here? Do you think it complies with the open-closed principle? Do you think the design should be changed?

[Answer]
The design is of poor quality here as it violates open-closed principle. The code uses a switch statement to cycle through all of the possible different types of goals. This  violates OCP as the implementation of any type of new goal would require changes to the existing Goal class, which could introduce bugs or make the code very complex and difficult to read. The code also introduces many attributes that are sometimes unused depending on the type of goal, which violates Single Responsibility Principle, as the class may be responsible for too many different types of goals. The design should thus be changed to fix these issues.

> ii. If you think the design is sufficient as it is, justify your decision. If you think the answer is no, pick a suitable Design Pattern that would improve the quality of the code and refactor the code accordingly.

[Briefly explain what you did]
I think the design is very insufficient. A design pattern that could be used to improve the quality of the code is the Composite pattern, with a tiny sprinkle of the template pattern. The composite pattern can be used so that the toString and achieved nodes can be used for any type of goal. The leaf nodes are the goals including exit, treasure and boulder, while the composite nodes are the And and Or operands for complex goals. The different types of goals were also separated based on the attributes it requires, where we have 3 abstract classes - Goal, GoalWithTarget and GoalsOperand. The latter 2 are subclasses of Goal. For the actual types of goals, And and Or are subclasses of GoalsOperand, Exit and Boulder are subclasses of Goal, and Treasure is subclass of GoalWithTarget. Since the achieved and toString had conditionally if statements that checked if the game had a player and if the goal was already achieved, we would have to repeat this check for every single goal class. We can circumvent this by using a template design pattern, where we have a conditional method for achieved and toString, and use them as methods inside another method which first does the conditional check.

### f) Open Refactoring

[Merge Request 1](/put/links/here)

[Briefly explain what you did]

[Merge Request 2](/put/links/here)

[Briefly explain what you did]

Add all other changes you made in the same format here:

## Task 2) Evolution of Requirements 👽

### a) Microevolution - Enemy Goal

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
