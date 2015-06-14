package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;

public class MctsTreeNode<DomainStateT extends MctsDomainState<DomainActionT>, DomainActionT> {

    private MctsTreeNode<DomainStateT, DomainActionT> parent;
    private DomainActionT incomingAction;
    private List<MctsTreeNode<DomainStateT, DomainActionT>> children;
    private DomainStateT representedState;
    private int visitCount;
    private double totalReward;
    private Cloner cloner;

    public MctsTreeNode(DomainStateT representedState, Cloner cloner) {
        this(representedState, null, null, cloner);
    }

    private MctsTreeNode(DomainStateT representedState, MctsTreeNode<DomainStateT, DomainActionT> parent,
                         DomainActionT incomingAction, Cloner cloner) {
        this.parent = parent;
        this.incomingAction = incomingAction;
        this.children = new ArrayList<>();
        this.representedState = representedState;
        this.visitCount = 0;
        this.totalReward = 0.0;
        this.cloner = cloner;
    }

    public MctsTreeNode<DomainStateT, DomainActionT> getParent() {
        return parent;
    }

    public DomainActionT getIncomingAction() {
        return incomingAction;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public double getTotalReward() {
        return totalReward;
    }

    public boolean isRootNode() {
        return parent == null;
    }

    public boolean representsTerminalState() {
        return representedState.isTerminal();
    }

    public boolean isFullyExpanded() {
        return representedState.getNumberOfAvailableActionsForCurrentPlayer() == children.size();
    }

    public List<DomainActionT> getUntriedActionsForRepresentedStatesCurrentPlayer() {
        List<DomainActionT> untriedActions = new ArrayList<>(representedState.getAvailableActionsForCurrentPlayer());
        untriedActions.removeAll(getTriedActionsForCurrentPlayer());
        return untriedActions;
    }

    private List<DomainActionT> getTriedActionsForCurrentPlayer() {
        List<DomainActionT> triedActions = new ArrayList<>();
        for(MctsTreeNode<DomainStateT, DomainActionT> child : children) {
            triedActions.add(child.getIncomingAction());
        }
        return triedActions;
    }

    public MctsTreeNode<DomainStateT, DomainActionT> addNewChildFromAction(DomainActionT action) {
        if(isUntriedAction(action)) {
            return addNewChildFromUntriedAction(action);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    private boolean isUntriedAction(DomainActionT action) {
        return getUntriedActionsForRepresentedStatesCurrentPlayer().contains(action);
    }

    private MctsTreeNode<DomainStateT, DomainActionT> addNewChildFromUntriedAction(DomainActionT untriedAction) {
        DomainStateT newState = getNewStateFromAction(untriedAction);
        return createNewChildInstance(newState, untriedAction);
    }

    private DomainStateT getNewStateFromAction(DomainActionT action) {
        DomainStateT representedStateClone = deepCloneRepresentedState();
        representedStateClone.performActionForCurrentPlayer(action);
        return representedStateClone;
    }

    public DomainStateT deepCloneRepresentedState() {
        return cloner.deepClone(representedState);
    }

    private MctsTreeNode<DomainStateT, DomainActionT> createNewChildInstance(DomainStateT representedState,
                                                                     DomainActionT incomingAction) {
        MctsTreeNode<DomainStateT, DomainActionT> child = new MctsTreeNode<>(representedState, this, incomingAction, cloner);
        children.add(child);
        return child;
    }

    public void updateDomainTheoreticValue(double rewardAddend) {
        visitCount += 1;
        totalReward += rewardAddend;
    }
}