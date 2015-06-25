package io.github.nejc92.mcts;

import java.util.Collections;
import java.util.List;

public class Mcts<StateT extends MctsDomainState<ActionT, AgentT>, ActionT, AgentT extends MctsDomainAgent<StateT>> {

    private static final int FIRST_RANDOM_ACTION = 0;

    private int numberOfIterations;

    public Mcts(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public ActionT uctSearch(StateT state, double explorationParameter) {
        MctsTreeNode<StateT, ActionT, AgentT> rootNode = MctsTreeNode.createRootNode(state, explorationParameter);
        for (int i = 0; i < numberOfIterations; i++) {
            performOneMctsIteration(rootNode, state.getCurrentAgent());
        }
        return rootNode.getMostPromisingAction();
    }

    private void performOneMctsIteration(MctsTreeNode<StateT, ActionT, AgentT> rootNode, AgentT agentInvoking) {
        MctsTreeNode<StateT, ActionT, AgentT> selectedChildNode = treePolicy(rootNode);
        StateT terminalState = getTerminalStateFromDefaultPolicy(selectedChildNode, agentInvoking);
        backPropagate(selectedChildNode, terminalState);
    }

    private MctsTreeNode<StateT, ActionT, AgentT> treePolicy(MctsTreeNode<StateT, ActionT, AgentT> treeNode) {
        while (!treeNode.representsTerminalState()) {
            if (!treeNode.isFullyExpanded())
                return expand(treeNode);
            else
                treeNode = treeNode.getBestChild();
        }
        return treeNode;
    }

    private MctsTreeNode<StateT, ActionT, AgentT> expand(MctsTreeNode<StateT, ActionT, AgentT> treeNode) {
        ActionT randomUntriedAction = getRandomActionFromNodesUntriedActions(treeNode);
        return treeNode.addNewChildFromAction(randomUntriedAction);
    }

    private ActionT getRandomActionFromNodesUntriedActions(MctsTreeNode<StateT, ActionT, AgentT> treeNode) {
        List<ActionT> untriedActions = treeNode.getUntriedActionsForCurrentAgent();
        Collections.shuffle(untriedActions);
        return untriedActions.get(FIRST_RANDOM_ACTION);
    }

    private StateT getTerminalStateFromDefaultPolicy(
            MctsTreeNode<StateT, ActionT, AgentT> treeNode, AgentT agentInvoking) {
        StateT treeNodesStateClone = treeNode.getDeepCloneOfRepresentedState();
        return agentInvoking.getTerminalStateByPerformingSimulationFromState(treeNodesStateClone);
    }

    private void backPropagate(MctsTreeNode<StateT, ActionT, AgentT> treeNode, StateT terminalState) {
        while (treeNode != null) {
            double reward = calculateStatesRewardForNode(terminalState, treeNode);
            treeNode.updateDomainTheoreticValue(reward);
            treeNode = treeNode.getParentNode();
        }
    }

    private double calculateStatesRewardForNode(StateT terminalState, MctsTreeNode<StateT, ActionT, AgentT> treeNode) {
        // todo: don't violate law of Demeter
        AgentT representedStatesPreviousAgent = treeNode.getRepresentedStatesPreviousAgent();
        return representedStatesPreviousAgent.getRewardFromTerminalState(terminalState);
    }
}