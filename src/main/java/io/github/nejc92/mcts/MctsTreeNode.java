package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MctsTreeNode<ActionT, StateT extends MctsDomainState<ActionT>> {

    private static final double NO_EXPLORATION = 0;

    private MctsTreeNode<ActionT, StateT> parentNode;
    private ActionT incomingAction;
    private StateT representedState;
    private int visitCount;
    private double totalReward;
    private double explorationParameter;
    private List<MctsTreeNode<ActionT, StateT>> childNodes;
    private Cloner cloner;

    public static<ActionT, StateT extends MctsDomainState<ActionT>>
            MctsTreeNode<ActionT, StateT> createRootNode(StateT representedState, double explorationParameter) {
        Cloner cloner = new Cloner();
        return new MctsTreeNode<>(null, null, representedState, explorationParameter, cloner);
    }

    private MctsTreeNode(MctsTreeNode<ActionT, StateT> parentNode, ActionT incomingAction,
                         StateT representedState, double explorationParameter, Cloner cloner) {
        this.parentNode = parentNode;
        this.incomingAction = incomingAction;
        this.representedState = representedState;
        this.visitCount = 0;
        this.totalReward = 0.0;
        this.explorationParameter = explorationParameter;
        this.childNodes = new ArrayList<>();
        this.cloner = cloner;
    }

    public MctsTreeNode<ActionT, StateT> getParentNode() {
        return parentNode;
    }

    private ActionT getIncomingAction() {
        return incomingAction;
    }

    private int getVisitCount() {
        return visitCount;
    }

    public boolean representsTerminalState() {
        return representedState.isTerminal();
    }

    public MctsTreeNode<ActionT, StateT> addNewChildFromAction(ActionT action) {
        if (!isUntriedAction(action))
            throw new IllegalArgumentException("Invalid action passed as function parameter");
        else
            return addNewChildFromUntriedAction(action);
    }

    private boolean isUntriedAction(ActionT action) {
        return getUntriedActionsForCurrentAgent().contains(action);
    }

    public List<ActionT> getUntriedActionsForCurrentAgent() {
        List<ActionT> availableActions = representedState.getAvailableActionsForCurrentAgent();
        List<ActionT> untriedActions = new ArrayList<>(availableActions);
        List<ActionT> triedActions = getTriedActionsForCurrentAgent();
        untriedActions.removeAll(triedActions);
        return untriedActions;
    }

    private List<ActionT> getTriedActionsForCurrentAgent() {
        return childNodes.stream()
                .map(MctsTreeNode::getIncomingAction)
                .collect(Collectors.toList());
    }

    private MctsTreeNode<ActionT, StateT> addNewChildFromUntriedAction(ActionT incomingAction) {
        StateT childNodeState = getNewStateFromAction(incomingAction);
        return appendNewChildInstance(childNodeState, incomingAction);
    }

    private StateT getNewStateFromAction(ActionT action) {
        StateT representedStateClone = getDeepCloneOfRepresentedState();
        representedStateClone.performActionForCurrentAgent(action);
        return representedStateClone;
    }

    public StateT getDeepCloneOfRepresentedState() {
        return cloner.deepClone(representedState);
    }

    private MctsTreeNode<ActionT, StateT> appendNewChildInstance(StateT representedState, ActionT incomingAction) {
        MctsTreeNode<ActionT, StateT> childNode = new MctsTreeNode<>(
                this, incomingAction, representedState, explorationParameter, cloner);
        childNodes.add(childNode);
        return childNode;
    }

    public ActionT getMostPromisingAction() {
        validateBestChildComputable();
        MctsTreeNode<ActionT, StateT> bestChildWithoutExploration =
                getBestChildConfidentlyWithExploration(NO_EXPLORATION);
        return bestChildWithoutExploration.getIncomingAction();
    }

    public MctsTreeNode<ActionT, StateT> getBestChild() {
        validateBestChildComputable();
        return getBestChildConfidentlyWithExploration(explorationParameter);
    }

    private void validateBestChildComputable() {
        if (!isFullyExpanded())
            throw new UnsupportedOperationException("Operation not supported if node not fully expanded");
        else if (hasUnvisitedChild())
            throw new UnsupportedOperationException("Operation not supported if node contains an unvisited child");
    }

    public boolean isFullyExpanded() {
        return representedState.getNumberOfAvailableActionsForCurrentAgent() == childNodes.size();
    }

    private boolean hasUnvisitedChild () {
        return childNodes.stream()
                .anyMatch(MctsTreeNode::isUnvisited);
    }

    private boolean isUnvisited() {
        return visitCount == 0;
    }

    private MctsTreeNode<ActionT, StateT> getBestChildConfidentlyWithExploration(double explorationParameter) {
        return childNodes.stream()
                .max((node1, node2) -> Double.compare(
                        node1.calculateUctValue(explorationParameter),
                        node2.calculateUctValue(explorationParameter))).get();
    }

    private double calculateUctValue(double explorationParameter) {
        return totalReward / visitCount
               + explorationParameter * (Math.sqrt((2 * Math.log(getParentNode().getVisitCount())) / visitCount));
    }

    public void updateDomainTheoreticValue(double rewardAddend) {
        visitCount += 1;
        totalReward += rewardAddend;
    }
}