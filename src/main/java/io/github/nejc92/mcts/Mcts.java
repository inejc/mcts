package io.github.nejc92.mcts;

import java.util.Collections;
import java.util.List;

public class Mcts<ActionT, StateT extends MctsDomainState<ActionT>, AgentT extends MctsDomainAgent<StateT>> {

    private static final int FIRST_RANDOM_ACTION = 0;

    private int numberOfIterations;

    public Mcts(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public ActionT uctSearch(StateT state, double explorationParameter, AgentT agentInvoking) {
        MctsTreeNode<ActionT, StateT> rootNode = MctsTreeNode.createRootNode(state, explorationParameter);
        for (int i = 0; i < numberOfIterations; i++) {
            performOneMctsIteration(rootNode, agentInvoking);
        }
        return rootNode.getMostPromisingAction();
    }

    private void performOneMctsIteration(MctsTreeNode<ActionT, StateT> rootNode, AgentT agentInvoking) {
        MctsTreeNode<ActionT, StateT> selectedChildNode = treePolicy(rootNode);
        double reward = getRewardFromDefaultPolicy(selectedChildNode, agentInvoking);
        backPropagate(selectedChildNode, reward);
    }

    private MctsTreeNode<ActionT, StateT> treePolicy(MctsTreeNode<ActionT, StateT> treeNode) {
        while (!treeNode.representsTerminalState()) {
            if (!treeNode.isFullyExpanded())
                return expand(treeNode);
            else
                treeNode = treeNode.getBestChild();
        }
        return treeNode;
    }

    private MctsTreeNode<ActionT, StateT> expand(MctsTreeNode<ActionT, StateT> treeNode) {
        List<ActionT> untriedActions = treeNode.getUntriedActionsForCurrentAgent();
        ActionT randomUntriedAction = getRandomActionFrom(untriedActions);
        return treeNode.addNewChildFromAction(randomUntriedAction);
    }

    private ActionT getRandomActionFrom(List<ActionT> actions) {
        Collections.shuffle(actions);
        return actions.get(FIRST_RANDOM_ACTION);
    }

    private double getRewardFromDefaultPolicy(MctsTreeNode<ActionT, StateT> treeNode, AgentT agentInvoking) {
        StateT treeNodesStateClone = treeNode.getDeepCloneOfRepresentedState();
        return agentInvoking.getRewardByPerformingSimulationFromState(treeNodesStateClone);
    }

    private void backPropagate(MctsTreeNode<ActionT, StateT> treeNode, double simulationResult) {
        while (treeNode != null) {
            treeNode.updateDomainTheoreticValue(simulationResult);
            treeNode = treeNode.getParentNode();
        }
    }
}