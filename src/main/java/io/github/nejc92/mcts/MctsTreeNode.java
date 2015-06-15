package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;

public class MctsTreeNode<StateT extends MctsDomainState<ActionT>, ActionT> {

    private MctsTreeNode<StateT, ActionT> parentNode;
    private ActionT incomingAction;
    private List<MctsTreeNode<StateT, ActionT>> childNodes;
    private StateT representedState;
    private int visitCount;
    private double totalReward;
    private Cloner cloner;

    public MctsTreeNode(StateT representedState, Cloner cloner) {
        this(representedState, null, null, cloner);
    }

    private MctsTreeNode(StateT representedState, MctsTreeNode<StateT, ActionT> parentNode,
                         ActionT incomingAction, Cloner cloner) {
        this.parentNode = parentNode;
        this.incomingAction = incomingAction;
        this.childNodes = new ArrayList<>();
        this.representedState = representedState;
        this.visitCount = 0;
        this.totalReward = 0.0;
        this.cloner = cloner;
    }

    public MctsTreeNode<StateT, ActionT> getParentNode() {
        return parentNode;
    }

    public ActionT getIncomingAction() {
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
        return representedState.getNumberOfAvailableActionsForCurrentAgent() == childNodes.size();
    }

    public MctsTreeNode<StateT, ActionT> addNewChildFromAction(ActionT action) {
        if(isUntriedAction(action)) {
            return addNewChildFromUntriedAction(action);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    private boolean isUntriedAction(ActionT action) {
        return getUntriedActionsForCurrentPlayer().contains(action);
    }

    public List<ActionT> getUntriedActionsForCurrentPlayer() {
        List<ActionT> untriedActions = new ArrayList<>(representedState.getAvailableActionsForCurrentAgent());
        untriedActions.removeAll(getTriedActionsForCurrentPlayer());
        return untriedActions;
    }

    private List<ActionT> getTriedActionsForCurrentPlayer() {
        List<ActionT> triedActions = new ArrayList<>();
        for(MctsTreeNode<StateT, ActionT> childNode : childNodes) {
            triedActions.add(childNode.getIncomingAction());
        }
        return triedActions;
    }

    private MctsTreeNode<StateT, ActionT> addNewChildFromUntriedAction(ActionT incomingAction) {
        StateT childNodeState = getNewStateFromAction(incomingAction);
        return createNewChildInstance(childNodeState, incomingAction);
    }

    private StateT getNewStateFromAction(ActionT action) {
        StateT representedStateClone = deepCloneRepresentedState();
        representedStateClone.performActionForCurrentAgent(action);
        return representedStateClone;
    }

    public StateT deepCloneRepresentedState() {
        return cloner.deepClone(representedState);
    }

    private MctsTreeNode<StateT, ActionT> createNewChildInstance(StateT representedState, ActionT incomingAction) {
        MctsTreeNode<StateT, ActionT> childNode = new MctsTreeNode<>(representedState, this, incomingAction, cloner);
        childNodes.add(childNode);
        return childNode;
    }

    public void updateDomainTheoreticValue(double rewardAddend) {
        visitCount += 1;
        totalReward += rewardAddend;
    }
}