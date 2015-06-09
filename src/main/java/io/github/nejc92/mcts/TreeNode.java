package io.github.nejc92.mcts;

public class TreeNode <T extends DomainState, S extends StateReward> {
    private TreeNode<T, S> parent;
    private TreeNode<T, S> [] children;
    private T domainState;
    private int N;
    private S totalReward;

    public static <T extends DomainState, S extends StateReward> TreeNode<T, S> createRootNode(T domainState) {
        return new TreeNode <> (domainState);
    }

    private TreeNode(T domainState) {
        this.parent = null;
        this.domainState = domainState;
    }

    public TreeNode expand(){
        return parent;
    }

    public boolean representsTerminalState() {
        return domainState.isTerminal();
    }

    public <U extends DomainAction> U[] getUntriedActionsForRepresentedState(){
        return domainState.getAvailableActionsForCurrentPlayer();
    }

    public boolean isFullyExpanded() {
        return true;
    }
}