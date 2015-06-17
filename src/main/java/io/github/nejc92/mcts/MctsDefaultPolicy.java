package io.github.nejc92.mcts;

public interface MctsDefaultPolicy<StateT extends MctsDomainState> {

    double performPlayoutFromState(StateT state);
}