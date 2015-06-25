# Monte Carlo Tree Search methods
This is a Java implementation of Monte Carlo Tree Search methods. It is self-contained, domain-independent and can thus be easily used in any state-action domain. The project was developed for the purpose of my Bachelor’s thesis.

## Dependencies
JUnit4, Java cloning library

## Usage
Create the implementation of MctsDomainAgent.
```java
public class Player implements MctsDomainAgent<GameState> {...}
```
Create the implementation of MctsDomainState.
```java
public class GameState implements MctsDomainState<Action, Player> {...}
```
Create an instance of Mcts object and invoke uctSearch() to get the most promising action.
```java
Mcts<Action, GameState, Player> mcts = new Mcts<>(numberOfIterations);

Action mostPromisingAction = mcts.uctSearch(state, explorationParameter);
```
Examples can be found in the test directory.

##### Important
- Because of comparison state's method getAvailableActionsForCurrentAgent() must always either return the same instances of objects or return objects that override equals() and hashCode() methods.
- Before every tree expansion and simulation a deep clone of represented state is created. This can lead to performance issues and should be taken into account when implementing MctsDomainState.

## License
This project is licensed under the terms of the MIT license. See LICENSE.md.

## Authors
Nejc Ilenič