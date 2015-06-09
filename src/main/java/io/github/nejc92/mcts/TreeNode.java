package io.github.nejc92.mcts;

import java.util.List;
import java.util.ArrayList;

public class TreeNode<T extends DomainState> {
    private TreeNode<T> parent;
    private List<TreeNode<T>> children;
    private T domainState;
    private int numberOfVisits;
    private double totalReward;

    public static<T extends DomainState> TreeNode<T> createRootNode(T domainState) {
        return new TreeNode<> (domainState);
    }

    private TreeNode(T domainState) {
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

    public<S extends DomainAction> ArrayList<S> getUntriedActionsForRepresentedState(){
        return domainState.getAvailableActionsForCurrentPlayer();
    }

    public boolean isFullyExpanded() {
        return true;
    }

    public void updateAfterSimulation(){
        numberOfVisits += 1;
        totalReward += domainState.getReward();
    }
}