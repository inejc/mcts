package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MctsTreeNode<StateT extends MctsDomainState<ActionT, AgentT>, ActionT, AgentT extends MctsDomainAgent> {

    private final MctsTreeNode<StateT, ActionT, AgentT> parentNode;
    private final ActionT incomingAction;
    private final StateT representedState;
    private int visitCount;
    private double totalReward;
    private List<MctsTreeNode<StateT, ActionT, AgentT>> childNodes;
    private final Cloner cloner;

    protected MctsTreeNode(StateT representedState, Cloner cloner) {
        this(null, null, representedState, cloner);
    }

    private MctsTreeNode(MctsTreeNode<StateT, ActionT, AgentT> parentNode, ActionT incomingAction,
                         StateT representedState, Cloner cloner) {
        this.parentNode = parentNode;
        this.incomingAction = incomingAction;
        this.representedState = representedState;
        this.visitCount = 0;
        this.totalReward = 0.0;
        this.childNodes = new ArrayList<>();
        this.cloner = cloner;
    }

    protected MctsTreeNode<StateT, ActionT, AgentT> getParentNode() {
        return parentNode;
    }

    protected ActionT getIncomingAction() {
        return incomingAction;
    }

    protected int getVisitCount() {
        return visitCount;
    }

    protected int getParentsVisitCount() {
        return parentNode.getVisitCount();
    }

    protected List<MctsTreeNode<StateT, ActionT, AgentT>> getChildNodes() {
        return childNodes;
    }

    protected boolean hasChildNodes() {
        return childNodes.size() > 0;
    }

    protected boolean representsTerminalState() {
        return representedState.isTerminal();
    }

    protected AgentT getRepresentedStatesPreviousAgent() {
        return representedState.getPreviousAgent();
    }

    protected boolean representedStatesCurrentAgentHasAvailableActions() {
        return representedState.getNumberOfAvailableActionsForCurrentAgent() > 0;
    }

    protected boolean isFullyExpanded() {
        return representedState.getNumberOfAvailableActionsForCurrentAgent() == childNodes.size();
    }

    protected boolean hasUnvisitedChild () {
        return childNodes.stream()
                .anyMatch(MctsTreeNode::isUnvisited);
    }

    private boolean isUnvisited() {
        return visitCount == 0;
    }

    protected MctsTreeNode<StateT, ActionT, AgentT> addNewChildWithoutAction() {
        StateT childNodeState = getDeepCloneOfRepresentedState();
        childNodeState.skipCurrentAgent();
        return appendNewChildInstance(childNodeState, null);
    }

    protected MctsTreeNode<StateT, ActionT, AgentT> addNewChildFromAction(ActionT action) {
        if (!isUntriedAction(action))
            throw new IllegalArgumentException("Error: invalid action passed as function parameter");
        else
            return addNewChildFromUntriedAction(action);
    }

    private boolean isUntriedAction(ActionT action) {
        return getUntriedActionsForCurrentAgent().contains(action);
    }

    protected List<ActionT> getUntriedActionsForCurrentAgent() {
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

    private MctsTreeNode<StateT, ActionT, AgentT> addNewChildFromUntriedAction(ActionT incomingAction) {
        StateT childNodeState = getNewStateFromAction(incomingAction);
        return appendNewChildInstance(childNodeState, incomingAction);
    }

    private StateT getNewStateFromAction(ActionT action) {
        StateT representedStateClone = getDeepCloneOfRepresentedState();
        representedStateClone.performActionForCurrentAgent(action);
        return representedStateClone;
    }

    protected StateT getDeepCloneOfRepresentedState() {
        return cloner.deepClone(representedState);
    }

    private MctsTreeNode<StateT, ActionT, AgentT> appendNewChildInstance(
            StateT representedState, ActionT incomingAction) {
        MctsTreeNode<StateT, ActionT, AgentT> childNode = new MctsTreeNode<>(
                this, incomingAction, representedState, cloner);
        childNodes.add(childNode);
        return childNode;
    }

    protected void updateDomainTheoreticValue(double rewardAddend) {
        visitCount += 1;
        totalReward += rewardAddend;
    }

    protected double getDomainTheoreticValue() {
        return totalReward / visitCount;
    }
}