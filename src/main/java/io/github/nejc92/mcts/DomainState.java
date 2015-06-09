package io.github.nejc92.mcts;

public abstract class DomainState {
    private int numberOfPlayers;
    private int currentPlayer;

    public abstract boolean isTerminal();
    public abstract <T extends DomainAction> T[] getAvailableActionsForCurrentPlayer();
    public abstract <T extends DomainAction> DomainState performActionForCurrentPlayer(T action);
    public abstract <S extends StateReward> S getReward();
    public abstract DomainState clone();
}