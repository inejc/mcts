package io.github.nejc92.mcts;

import java.util.List;

public interface DomainState<T> {
    DomainState duplicate();
    boolean isTerminal();
    double getRewardForTerminalState();
    List<T> getAvailableActionsForCurrentPlayer();
    int getNumberOfAvailableActionsForCurrentPlayer();
    DomainState performActionForCurrentPlayer(T action);
}