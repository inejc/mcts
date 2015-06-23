# Monte Carlo Tree Search for the Scotland Yard board game
This is a Java implementation of Monte Carlo Tree Search method for the hide-and-seek board game with imperfect information. Nevertheless, the mcts component is self-contained, domain-independent and can thus be easily reused in other domains. The project was developed for the purpose of my Bachelor’s thesis.

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

Action mostPromisingAction = mcts.uctSearch(state, player, explorationParameter);
```

## License
This project is licensed under the terms of the MIT license. See LICENSE.md.

## Authors
Nejc Ilenič