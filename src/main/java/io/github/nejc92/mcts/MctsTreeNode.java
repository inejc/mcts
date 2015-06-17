package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MctsTreeNode<StateT extends MctsDomainState<ActionT>, ActionT> {

    private MctsTreeNode<StateT, ActionT> parentNode;
    private ActionT incomingAction;
    private StateT representedState;
    private int visitCount;
    private double totalReward;
    private double explorationParameter;
    private List<MctsTreeNode<StateT, ActionT>> childNodes;
    private Cloner cloner;

    public static<StateT extends MctsDomainState<ActionT>, ActionT> MctsTreeNode<StateT, ActionT> createRootNode(
            StateT representedState, double explorationParameter) {
        Cloner cloner = new Cloner();
        return new MctsTreeNode<>(null, null, representedState, explorationParameter, cloner);
    }

    private MctsTreeNode(MctsTreeNode<StateT, ActionT> parentNode, ActionT incomingAction, StateT representedState,
                         double explorationParameter, Cloner cloner) {
        this.parentNode = parentNode;
        this.incomingAction = incomingAction;
        this.representedState = representedState;
        this.visitCount = 0;
        this.totalReward = 0.0;
        this.explorationParameter = explorationParameter;
        this.childNodes = new ArrayList<>();
        this.cloner = cloner;
    }

    public MctsTreeNode<StateT, ActionT> getParentNode() {
        return parentNode;
    }

    private ActionT getIncomingAction() {
        return incomingAction;
    }

    private int getVisitCount() {
        return visitCount;
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
        if(!isUntriedAction(action))
            throw new IllegalArgumentException("Invalid action passed as function parameter");
        else
            return addNewChildFromUntriedAction(action);
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
                this, incomingAction, representedState, explorationParameter, cloner);
        childNodes.add(childNode);
        return childNode;
    }

    public ActionT returnMostPromisingAction() {
        return returnBestChild().getIncomingAction();
    }

    public MctsTreeNode<StateT, ActionT> returnBestChild() {
        if (hasUnvisitedChild())
            throw new UnsupportedOperationException("Operation not supported if node contains an unvisited child");
        else if (!isExpanded())
            throw new UnsupportedOperationException("Operation not supported on unexpanded node");
        else
            return returnBestChildConfidently();
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

    private MctsTreeNode<StateT, ActionT> returnBestChildConfidently() {
        return childNodes.stream()
                .max((node1, node2) -> Double.compare(node1.calculateUctValue(), node2.calculateUctValue()))
                .get();
    }

    private double calculateUctValue() {
        return totalReward / visitCount
               + explorationParameter * (Math.sqrt((2 * Math.log(getParentNode().getVisitCount())) / visitCount));
    }

    public void updateDomainTheoreticValue(double rewardAddend) {
        visitCount += 1;
        totalReward += rewardAddend;
    }
}