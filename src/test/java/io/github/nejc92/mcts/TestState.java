package io.github.nejc92.mcts;

import java.util.List;

public class TestState implements MctsDomainState<TestAgent, String> {

    private List<String> availableActions;

    public TestState(List<String> availableActions) {
        this.availableActions = availableActions;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public TestAgent getCurrentAgent() {
        return new TestAgent();
    }

    @Override
    public List<String> getAvailableActionsForCurrentAgent() {
        return availableActions;
    }

    @Override
    public int getNumberOfAvailableActionsForCurrentAgent() {
        return getAvailableActionsForCurrentAgent().size();
    }

    @Override
    public MctsDomainState performActionForCurrentAgent(String action) {
        return this;
    }
}