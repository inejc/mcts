package io.github.nejc92.mcts;

import java.util.List;

public interface MctsDomainState<T> {
    MctsDomainState duplicate();

    boolean isTerminal();

    double getRewardForTerminalState();

    List<T> getAvailableActionsForCurrentPlayer();

    int getNumberOfAvailableActionsForCurrentPlayer();

    MctsDomainState performActionForCurrentPlayer(T action);
}