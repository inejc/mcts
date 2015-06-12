package io.github.nejc92.mcts;

import java.util.List;
import java.util.ArrayList;

public class MctsTreeNode<DomainStateT extends MctsDomainState<DomainActionT>, DomainActionT> {

    private MctsTreeNode<DomainStateT, DomainActionT> parent;
    private List<MctsTreeNode<DomainStateT, DomainActionT>> children;
    private DomainStateT domainState;
    private DomainActionT incomingAction;
    private int numberOfVisits;
    private double totalReward;

    //root node
    public MctsTreeNode(DomainStateT domainState) {
        this.children = new ArrayList<>();
        this.domainState = domainState;
        this.numberOfVisits = 0;
        this.totalReward = 0.0;
    }

    public MctsTreeNode<DomainStateT, DomainActionT> addNewChild(){
        //DomainStateT newState = domainState.getDeepClone();
        return parent;
    }

    public boolean representsTerminalState() {
        return domainState.isTerminal();
    }

    public List<DomainActionT> getUntriedActionsForRepresentedState(){
        List<DomainActionT> untriedActions = domainState.getAvailableActionsForCurrentPlayer();
        for(MctsTreeNode<DomainStateT, DomainActionT> child : children) {
            untriedActions.remove(child.getStatesIncomingAction());
        }
        return untriedActions;
    }

    public DomainActionT getStatesIncomingAction(){
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