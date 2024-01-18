# 1. How to run
```
gradle clean build run
```

# 2. Implemented feature

## 2.1. Difficulty selection
When the game starts, the user is asked to select a difficulty. The difficulty is selected by choosing an option from the dropdown menu. The options are: Easy, Medium, Hard. After choosing the wanted difficulty, the user can press the "OK" button to start the game. The default difficulty is Easy.

## 2.2. Time and Score
The Time and Score of the current game can be seen on the top of the screen. The Time is displayed in the format "TIME: mm:ss" on the top left corner. The Score is displayed in the format "SCORE: n" on the top right corner. 
The Score is updated every time the player's projectile hit an enemy or another projectile.
The Time is updated every second until the game ends.

## 2.3. Undo and Cheat
The player can set the state of the game to before the last shot by pressing the key "R" on the keyboard.
The player can cheat by pressing one of the four keys "1", "2", "3" or "4" on the keyboard. Each key will remove a certain kind of objects:
- "1": remove all slow projectiles
- "2": remove all fast projectiles
- "3": remove all slow enemies
- "4": remove all fast enemies

# 3. Design patterns
The extensions implementation includes 3 design patterns: Observer, Prototype and Memento.
## 3.1. Observer
The files corresponding to this design patterns are located in the package "observer" and engine/GameEngine.java.
The classes corresponding to this design patterns are:
- Subject (interface)
- Observer (interface)
- GameClock (class)
- GameEngine (class)
- GameEngine (class)

## 3.2. Prototype
The files corresponding to this design patterns are located in the packages "prototype", gameobject/Enemy.java, gameobject/Bunker.java, factory/Projectile.java, factory/EnemtProjectile.java, factory/PlayerProjectile.java and entities/Player.java.
The classes corresponding to this design patterns are:
- Prototype (interface)
- Enemy (class)
- Bunker (class)
- Projectile (class)
- EnemyProjectile (class)
- PlayerProjectile (class)
- Player (class)

## 3.3. Memento
The files corresponding to this design patterns are located in the package "memento", engine/GameEngine.java, and engine/KeyboardInputHandler.java.
The classes corresponding to this design patterns are:
- Memento (interface)
- Originator (interface)
- Caretaker (interface)
- GEngineMemento (class)
- GameEngine (class)
- KeyboardInputHandler (class)