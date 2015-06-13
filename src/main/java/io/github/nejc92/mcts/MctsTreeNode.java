package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;
import java.util.List;
import java.util.ArrayList;

public class MctsTreeNode<DomainStateT extends MctsDomainState<DomainActionT>, DomainActionT> {

    private MctsTreeNode<DomainStateT, DomainActionT> parent;
    private DomainActionT incomingAction;
    private List<MctsTreeNode<DomainStateT, DomainActionT>> children;
    private DomainStateT representedState;
    private int visitCount;
    private double totalReward;

    public MctsTreeNode(DomainStateT representedState) {
        this(representedState, null, null);
    }

    private MctsTreeNode(DomainStateT representedState, MctsTreeNode<DomainStateT, DomainActionT> parent,
                         DomainActionT incomingAction) {
        this.parent = parent;
        this.incomingAction = incomingAction;
        this.children = new ArrayList<>();
        this.representedState = representedState;
        this.visitCount = 0;
        this.totalReward = 0.0;
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
        List<DomainActionT> availableActions = representedState.getAvailableActionsForCurrentPlayer();
        return removeTriedActionsFromAvailableActions(availableActions);
    }

    private List<DomainActionT> removeTriedActionsFromAvailableActions(List<DomainActionT> availableActions) {
        for(MctsTreeNode<DomainStateT, DomainActionT> child : children) {
            availableActions.remove(child.getIncomingAction());
        }
        return availableActions;
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

    private MctsTreeNode<DomainStateT, DomainActionT> addNewChildFromUntriedAction(DomainActionT untriedAction){
        DomainStateT newState = getNewStateFromAction(untriedAction);
        return createNewChild(newState, untriedAction);
    }

    private DomainStateT getNewStateFromAction(DomainActionT action){
        DomainStateT representedStateClone = deepCloneRepresentedState();
        representedStateClone.performActionForCurrentPlayer(action);
        return representedStateClone;
    }

    public DomainStateT deepCloneRepresentedState() {
        Cloner cloner = new Cloner();
        return cloner.deepClone(representedState);
    }

    private MctsTreeNode<DomainStateT, DomainActionT> createNewChild(DomainStateT representedState, DomainActionT incomingAction) {
        MctsTreeNode<DomainStateT, DomainActionT> child = new MctsTreeNode<>(representedState, this, incomingAction);
        children.add(child);
        return child;
    }

    public void updateDomainTheoreticValue(double rewardAddend) {
        visitCount += 1;
        totalReward += rewardAddend;
    }
}