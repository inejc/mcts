package io.github.nejc92.mcts;

import java.util.List;
import java.util.ArrayList;

public class MctsTreeNode<T, S extends MctsDomainState<T>> {
    private MctsTreeNode<T, S> parent;
    private List<MctsTreeNode<T, S>> children;
    private S domainState;
    private T incomingAction;
    private int numberOfVisits;
    private double totalReward;

    public MctsTreeNode(S domainState) {
        this.parent = null;
        this.children = new ArrayList<>();
        this.domainState = domainState;
        this.numberOfVisits = 0;
        this.totalReward = 0.0;
    }

    public MctsTreeNode expand(){
        return parent;
    }

    public boolean representsTerminalState() {
        return domainState.isTerminal();
    }

    public List<T> getUntriedActionsForRepresentedState(){
        return domainState.getAvailableActionsForCurrentPlayer();
    }

    public T getStatesIncomingAction(){
        return incomingAction;
    }

    public boolean isFullyExpanded() {
        return domainState.getNumberOfAvailableActionsForCurrentPlayer() == children.size();
    }

    public void updateAfterSimulation(){
        numberOfVisits += 1;
        totalReward += domainState.getRewardForTerminalState();
    }
}