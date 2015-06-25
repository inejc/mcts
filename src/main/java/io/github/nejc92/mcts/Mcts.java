package io.github.nejc92.mcts;

import java.util.Collections;
import java.util.List;

public class Mcts<ActionT, StateT extends MctsDomainState<ActionT, AgentT>, AgentT extends MctsDomainAgent<StateT>> {

    private static final int FIRST_RANDOM_ACTION = 0;

    private int numberOfIterations;

    public Mcts(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public ActionT uctSearch(StateT state, double explorationParameter) {
        MctsTreeNode<ActionT, StateT, AgentT> rootNode = MctsTreeNode.createRootNode(state, explorationParameter);
        for (int i = 0; i < numberOfIterations; i++) {
            performOneMctsIteration(rootNode, state.getCurrentAgent());
        }
        return rootNode.getMostPromisingAction();
    }

    private void performOneMctsIteration(MctsTreeNode<ActionT, StateT, AgentT> rootNode, AgentT agentInvoking) {
        MctsTreeNode<ActionT, StateT, AgentT> selectedChildNode = treePolicy(rootNode);
        StateT terminalState = getTerminalStateFromDefaultPolicy(selectedChildNode, agentInvoking);
        backPropagate(selectedChildNode, terminalState);
    }

    private MctsTreeNode<ActionT, StateT, AgentT> treePolicy(MctsTreeNode<ActionT, StateT, AgentT> treeNode) {
        while (!treeNode.representsTerminalState()) {
            if (!treeNode.isFullyExpanded())
                return expand(treeNode);
            else
                treeNode = treeNode.getBestChild();
        }
        return treeNode;
    }

    private MctsTreeNode<ActionT, StateT, AgentT> expand(MctsTreeNode<ActionT, StateT, AgentT> treeNode) {
        ActionT randomUntriedAction = getRandomActionFromNodesUntriedActions(treeNode);
        return treeNode.addNewChildFromAction(randomUntriedAction);
    }

    private ActionT getRandomActionFromNodesUntriedActions(MctsTreeNode<ActionT, StateT, AgentT> treeNode) {
        List<ActionT> untriedActions = treeNode.getUntriedActionsForCurrentAgent();
        Collections.shuffle(untriedActions);
        return untriedActions.get(FIRST_RANDOM_ACTION);
    }

    private StateT getTerminalStateFromDefaultPolicy(
            MctsTreeNode<ActionT, StateT, AgentT> treeNode, AgentT agentInvoking) {
        StateT treeNodesStateClone = treeNode.getDeepCloneOfRepresentedState();
        return agentInvoking.getTerminalStateByPerformingSimulationFromState(treeNodesStateClone);
    }

    private void backPropagate(MctsTreeNode<ActionT, StateT, AgentT> treeNode, StateT terminalState) {
        while (treeNode != null) {
            double reward = calculateStatesRewardForNode(terminalState, treeNode);
            treeNode.updateDomainTheoreticValue(reward);
            treeNode = treeNode.getParentNode();
        }
    }

    private double calculateStatesRewardForNode(StateT terminalState, MctsTreeNode<ActionT, StateT, AgentT> treeNode) {
        // todo: don't violate law of Demeter
        AgentT representedStatesPreviousAgent = treeNode.getRepresentedStatesPreviousAgent();
        return representedStatesPreviousAgent.getRewardFromTerminalState(terminalState);
    }
}