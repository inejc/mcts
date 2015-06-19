package io.github.nejc92.mcts.examples;

import io.github.nejc92.mcts.MctsDomainState;

import java.util.List;

public class SimpleState implements MctsDomainState<String> {

    private List<String> availableActions;

    public SimpleState(List<String> availableActions) {
        this.availableActions = availableActions;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public int getNumberOfAvailableActionsForCurrentAgent() {
        return getAvailableActionsForCurrentAgent().size();
    }

    @Override
    public List<String> getAvailableActionsForCurrentAgent() {
        return availableActions;
    }

    @Override
    public MctsDomainState performActionForCurrentAgent(String action) {
        return this;
    }
}