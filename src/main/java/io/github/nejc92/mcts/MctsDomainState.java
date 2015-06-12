package io.github.nejc92.mcts;

import java.util.List;

public interface MctsDomainState<DomainActionT> {

    //MctsDomainState getDeepClone();
    boolean isTerminal();
    double getRewardForTerminalState();
    List<DomainActionT> getAvailableActionsForCurrentPlayer();
    int getNumberOfAvailableActionsForCurrentPlayer();
    MctsDomainState performActionForCurrentPlayer(DomainActionT action);
}