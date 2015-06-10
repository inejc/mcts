package io.github.nejc92.mcts;

import java.util.List;
import java.util.ArrayList;

public class TreeNode<T, S extends DomainState<T>> {
    private TreeNode<T, S> parent;
    private List<TreeNode<T, S>> children;
    private S domainState;
    private T incomingAction;
    private int numberOfVisits;
    private double totalReward;

    public TreeNode(S domainState) {
        this.parent = null;
        this.children = new ArrayList<>();
        this.domainState = domainState;
        this.numberOfVisits = 0;
        this.totalReward = 0.0;
    }

    public TreeNode expand(){
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