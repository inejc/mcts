package io.github.nejc92.mcts;

import java.util.List;

public interface MctsDomainState<ActionT> {

    boolean isTerminal();
    List<ActionT> getAvailableActionsForCurrentAgent();
    int getNumberOfAvailableActionsForCurrentAgent();
    MctsDomainState performActionForCurrentAgent(ActionT action);
}