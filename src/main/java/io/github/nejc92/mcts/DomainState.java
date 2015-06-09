package io.github.nejc92.mcts;

import java.util.ArrayList;

public abstract class DomainState {
    private int numberOfPlayers;
    private int currentPlayer;

    public abstract boolean isTerminal();
    public abstract<T extends DomainAction> ArrayList<T> getAvailableActionsForCurrentPlayer();
    public abstract<T extends DomainAction> DomainState performActionForCurrentPlayer(T action);
    public abstract double getReward();
    public abstract DomainState clone();
}