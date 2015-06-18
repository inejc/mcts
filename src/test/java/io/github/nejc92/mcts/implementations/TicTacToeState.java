package io.github.nejc92.mcts.implementations;

import io.github.nejc92.mcts.MctsDomainState;

import java.util.List;

public class TicTacToeState implements MctsDomainState<String> {

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public List<String> getAvailableActionsForCurrentAgent() {
        return null;
    }

    @Override
    public int getNumberOfAvailableActionsForCurrentAgent() {
        return 0;
    }

    @Override
    public MctsDomainState performActionForCurrentAgent(String action) {
        return null;
    }
}