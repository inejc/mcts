package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;

public class MctsTreeNode<DomainStateT extends MctsDomainState<DomainActionT>, DomainActionT> {

    private MctsTreeNode<DomainStateT, DomainActionT> parentNode;
    private DomainActionT incomingAction;
    private List<MctsTreeNode<DomainStateT, DomainActionT>> childNodes;
    private DomainStateT representedState;
    private int visitCount;
    private double totalReward;
    private Cloner cloner;

    public MctsTreeNode(DomainStateT representedState, Cloner cloner) {
        this(representedState, null, null, cloner);
    }

    private MctsTreeNode(DomainStateT representedState, MctsTreeNode<DomainStateT, DomainActionT> parentNode,
                         DomainActionT incomingAction, Cloner cloner) {
        this.parentNode = parentNode;
        this.incomingAction = incomingAction;
        this.childNodes = new ArrayList<>();
        this.representedState = representedState;
        this.visitCount = 0;
        this.totalReward = 0.0;
        this.cloner = cloner;
    }

    public MctsTreeNode<DomainStateT, DomainActionT> getParentNode() {
        return parentNode;
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
        return parentNode == null;
    }

    public boolean representsTerminalState() {
        return representedState.isTerminal();
    }

    public boolean isFullyExpanded() {
        return representedState.getNumberOfAvailableActionsForCurrentPlayer() == childNodes.size();
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
        return getUntriedActionsForCurrentPlayer().contains(action);
    }

    public List<DomainActionT> getUntriedActionsForCurrentPlayer() {
        List<DomainActionT> untriedActions = new ArrayList<>(representedState.getAvailableActionsForCurrentPlayer());
        untriedActions.removeAll(getTriedActionsForCurrentPlayer());
        return untriedActions;
    }

    private List<DomainActionT> getTriedActionsForCurrentPlayer() {
        List<DomainActionT> triedActions = new ArrayList<>();
        for(MctsTreeNode<DomainStateT, DomainActionT> childNode : childNodes) {
            triedActions.add(childNode.getIncomingAction());
        }
        return triedActions;
    }

    private MctsTreeNode<DomainStateT, DomainActionT> addNewChildFromUntriedAction(DomainActionT incomingAction) {
        DomainStateT childNodeState = getNewStateFromAction(incomingAction);
        return createNewChildInstance(childNodeState, incomingAction);
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
        MctsTreeNode<DomainStateT, DomainActionT> childNode = new MctsTreeNode<>(representedState,
                                                                                 this, incomingAction, cloner);
        childNodes.add(childNode);
        return childNode;
    }

    public void updateDomainTheoreticValue(double rewardAddend) {
        visitCount += 1;
        totalReward += rewardAddend;
    }
}