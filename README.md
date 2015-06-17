# Monte Carlo Tree Search for the Scotland Yard board game
This is a Java implementation of Monte Carlo Tree Search method for the hide-and-seek board game with imperfect information. Nevertheless, the mcts component is self-contained, domain-independent and can thus be easily reused in other domains. The project was developed for the purpose of my Bachelor’s thesis.

## Dependencies
JUnit4, Java cloning library

## Usage
Create the implementation of MctsDomainState for a specific domain state.
```java
public class DomainState extends MctsDomainState<ActionT> {...}
```
Create the implementation of MctsDefaultPolicy for a statistically biased playout.
```java
public class StatisticallyBiasedPlayout extends MctsDefaultPolicy<DomainState> {...}
```
Create an instance of Mcts and call uctSearch() to get the most promising action.
```java
Mcts<DomainState, ActionT, StatisticallyBiasedPlayout> mcts = new Mcts<>(numberOfIterations);

ActionT mostPromisingAction = mcts.uctSearch(state, explorationParameter, defaultPolicy);
```

## License
This project is licensed under the terms of the MIT license. See LICENSE.md.

## Authors
Nejc Ilenič