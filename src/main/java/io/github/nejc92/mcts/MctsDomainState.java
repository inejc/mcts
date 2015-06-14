package io.github.nejc92.mcts;

import java.util.List;

public interface MctsDomainState<AgentT extends MctsDomainAgent, ActionT> {

    boolean isTerminal();
    AgentT getCurrentAgent();
    List<ActionT> getAvailableActionsForCurrentAgent();
    int getNumberOfAvailableActionsForCurrentAgent();
    MctsDomainState performActionForCurrentAgent(ActionT action);
}