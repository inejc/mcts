package io.github.nejc92.mcts;

import java.util.ArrayList;

public abstract class DomainState {
    public abstract DomainState clone();
    public abstract boolean isTerminal();
    public abstract double getReward();
    public abstract<T> ArrayList<T> getAvailableActionsForCurrentPlayer();
    public abstract int getNumberOfAvailableActionsForCurrentPlayer();
    public abstract<T> DomainState performActionForCurrentPlayer(T action);
}