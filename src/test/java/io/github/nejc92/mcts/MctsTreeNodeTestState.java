package io.github.nejc92.mcts;

import java.util.List;

public class MctsTreeNodeTestState implements MctsDomainState<MctsTreeNodeTestAction> {

    private List<MctsTreeNodeTestAction> availableActions;

    public MctsTreeNodeTestState(List<MctsTreeNodeTestAction> availableActions) {
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
    public List<MctsTreeNodeTestAction> getAvailableActionsForCurrentPlayer() {
        return availableActions;
    }

    @Override
    public int getNumberOfAvailableActionsForCurrentPlayer() {
        return getAvailableActionsForCurrentPlayer().size();
    }

    @Override
    public MctsDomainState performActionForCurrentPlayer(MctsTreeNodeTestAction action) {
        return this;
    }
}