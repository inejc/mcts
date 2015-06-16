package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MctsTreeNode<StateT extends MctsDomainState<ActionT>, ActionT> {

    private MctsTreeNode<StateT, ActionT> parentNode;
    private ActionT incomingAction;
    private List<MctsTreeNode<StateT, ActionT>> childNodes;
    private StateT representedState;
    private int visitCount;
    private double totalReward;
    private double explorationParameter;
    private Cloner cloner;

    public MctsTreeNode(StateT representedState, double explorationParameter, Cloner cloner) {
        this(representedState, null, null, explorationParameter, cloner);
    }

    private MctsTreeNode(StateT representedState, MctsTreeNode<StateT, ActionT> parentNode, ActionT incomingAction,
                         double explorationParameter, Cloner cloner) {
        this.parentNode = parentNode;
        this.incomingAction = incomingAction;
        this.childNodes = new ArrayList<>();
        this.representedState = representedState;
        this.visitCount = 0;
        this.totalReward = 0.0;
        this.explorationParameter = explorationParameter;
        this.cloner = cloner;
    }

    private int getVisitCount() {
        return visitCount;
    }

    public MctsTreeNode<StateT, ActionT> getParentNode() {
        if(isRootNode()) {
            throw new UnsupportedOperationException("Operation not supported on root node");
        }
        else {
            return parentNode;
        }
    }

    public ActionT getIncomingAction() {
        if(isRootNode()) {
            throw new UnsupportedOperationException("Operation not supported on root node");
        }
        else {
            return incomingAction;
        }
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
        if(!isUntriedAction(action)) {
            throw new IllegalArgumentException("Invalid action passed as function parameter");
        }
        else {
            return addNewChildFromUntriedAction(action);
        }
    }

    private boolean isUntriedAction(ActionT action) {
        return getUntriedActionsForCurrentAgent().contains(action);
    }

    public List<ActionT> getUntriedActionsForCurrentAgent() {
        List<ActionT> untriedActions = new ArrayList<>(representedState.getAvailableActionsForCurrentAgent());
        untriedActions.removeAll(getTriedActionsForCurrentAgent());
        return untriedActions;
    }

    private List<ActionT> getTriedActionsForCurrentAgent() {
        return childNodes.stream()
                .map(MctsTreeNode::getIncomingAction)
                .collect(Collectors.toList());
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
        MctsTreeNode<StateT, ActionT> childNode = new MctsTreeNode<>(
                representedState, this, incomingAction, explorationParameter, cloner);
        childNodes.add(childNode);
        return childNode;
    }

    public void updateDomainTheoreticValue(double rewardAddend) {
        visitCount += 1;
        totalReward += rewardAddend;
    }

    public ActionT getMostPromisingAction() {
        return getBestChild().getIncomingAction();
    }

    public MctsTreeNode<StateT, ActionT> getBestChild() {
        if (hasUnvisitedChild()) {
            throw new UnsupportedOperationException("Operation not supported if node contains an unvisited child");
        }
        else if (!isExpanded()) {
            throw new UnsupportedOperationException("Operation not supported on unexpanded node");
        }
        else {
            return getBestChildConfidently();
        }
    }

    private boolean hasUnvisitedChild () {
        return childNodes.stream()
                .anyMatch(MctsTreeNode::isUnvisited);
    }

    private boolean isUnvisited() {
        return visitCount == 0;
    }

    private boolean isExpanded() {
        return childNodes.size() != 0;
    }

    private MctsTreeNode<StateT, ActionT> getBestChildConfidently() {
        return childNodes.stream()
                .max((node1, node2) -> Double.compare(node1.calculateUctValue(), node2.calculateUctValue()))
                .get();
    }

    private double calculateUctValue() {
        return totalReward / visitCount
               + explorationParameter * (Math.sqrt((2 * Math.log(getParentNode().getVisitCount())) / visitCount));
    }
}