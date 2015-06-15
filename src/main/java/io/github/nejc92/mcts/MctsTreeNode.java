package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;

import javax.naming.OperationNotSupportedException;
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

    private MctsTreeNode(StateT representedState, MctsTreeNode<StateT, ActionT> parentNode, ActionT incomingAction,
                         Cloner cloner) {
        this.parentNode = parentNode;
        this.incomingAction = incomingAction;
        this.childNodes = new ArrayList<>();
        this.representedState = representedState;
        this.visitCount = 0;
        this.totalReward = 0.0;
        this.cloner = cloner;
    }

    public MctsTreeNode<StateT, ActionT> getParentNode() {
        if(!isRootNode()) {
            return parentNode;
        }
        else {
            throw new UnsupportedOperationException("Operation not supported on unexpanded node");
        }
    }

    public ActionT getIncomingAction() {
        if(!isRootNode()) {
            return incomingAction;
        }
        else {
            throw new UnsupportedOperationException("Operation not supported on unexpanded node");
        }
    }

    public boolean isRootNode() {
        return parentNode == null;
    }

    public boolean representsTerminalState() {
        return representedState.isTerminal();
    }

    // todo: no state's available actions? + exception messages + change to this.methodName() where necessary
    public boolean isFullyExpanded() {
        return representedState.getNumberOfAvailableActionsForCurrentAgent() == childNodes.size();
    }

    public MctsTreeNode<StateT, ActionT> addNewChildFromAction(ActionT action) {
        if(isUntriedAction(action)) {
            return addNewChildFromUntriedAction(action);
        }
        else {
            throw new IllegalArgumentException("Invalid action passed as function parameter");
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

    public ActionT getMostPromisingAction() {
        return calculateBestChildWithExplorationParameter(0).getIncomingAction();
    }

    public MctsTreeNode<StateT, ActionT> calculateBestChildWithExplorationParameter(double parameter) {
        if (!isExpanded()) {
            throw new UnsupportedOperationException("Operation not supported on unexpanded node");
        }
        else if (this.hasUnvisitedChild()) {
            throw new UnsupportedOperationException("Operation not supported if node contains an unvisited child");
        }
        else {
            return confidentlyCalculateBestChildWithExplorationParameter(parameter);
        }
    }

    private boolean isExpanded() {
        return childNodes.size() != 0;
    }

    private boolean hasUnvisitedChild () {
        for(MctsTreeNode<StateT, ActionT> childNode : childNodes) {
            if (childNode.notYetVisited()) {
                return true;
            }
        }
        return false;
    }

    private boolean notYetVisited() {
        return visitCount == 0;
    }

    private MctsTreeNode<StateT, ActionT> confidentlyCalculateBestChildWithExplorationParameter(double parameter) {
//        double uctValue = Double.MIN_VALUE;
//        for(MctsTreeNode<StateT, ActionT> childNode : childNodes) {
//
//        }
        return null;
    }
}