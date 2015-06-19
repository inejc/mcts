package io.github.nejc92.mcts;

import java.util.List;

public interface MctsDomainState<ActionT> {

    boolean isTerminal();
    int getNumberOfAvailableActionsForCurrentAgent();
    List<ActionT> getAvailableActionsForCurrentAgent();
    MctsDomainState performActionForCurrentAgent(ActionT action);
}