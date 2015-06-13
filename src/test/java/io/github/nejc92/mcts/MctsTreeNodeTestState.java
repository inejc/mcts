package io.github.nejc92.mcts;

import java.util.List;

public class MctsTreeNodeTestState implements MctsDomainState<String> {

    private List<String> availableActions;

    public MctsTreeNodeTestState(List<String> availableActions) {
        this.availableActions = availableActions;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public double getRewardForTerminalState() {
        return 0;
    }

    @Override
    public List<String> getAvailableActionsForCurrentPlayer() {
        return availableActions;
    }

    @Override
    public int getNumberOfAvailableActionsForCurrentPlayer() {
        return getAvailableActionsForCurrentPlayer().size();
    }

    @Override
    public MctsDomainState performActionForCurrentPlayer(String action) {
        return this;
    }
}