package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;

import java.util.Collections;
import java.util.List;

public class Mcts<StateT extends MctsDomainState<ActionT, AgentT>, ActionT, AgentT extends MctsDomainAgent<StateT>> {

    private static final double NO_EXPLORATION = 0;

    private final int numberOfIterations;
    private final Cloner cloner;

    public static<StateT extends MctsDomainState<ActionT, AgentT>, ActionT, AgentT extends MctsDomainAgent<StateT>>
        Mcts<StateT, ActionT, AgentT> initializeIterations(int numberOfIterations) {
            Cloner cloner = new Cloner();
            return new Mcts<>(numberOfIterations, cloner);
    }

    private Mcts(int numberOfIterations, Cloner cloner) {
        this.numberOfIterations = numberOfIterations;
        this.cloner = cloner;
    }

    public ActionT uctSearchWithExploration(StateT state, double explorationParameter) {
        MctsTreeNode<StateT, ActionT, AgentT> rootNode = new MctsTreeNode<>(state, cloner);
        for (int i = 0; i < numberOfIterations; i++) {
            performMctsIteration(rootNode, state.getCurrentAgent(), explorationParameter);
        }
        return getNodesMostPromisingAction(rootNode);
    }

    private void performMctsIteration(MctsTreeNode<StateT, ActionT, AgentT> rootNode, AgentT agentInvoking,
                                      double explorationParameter) {
        MctsTreeNode<StateT, ActionT, AgentT> selectedChildNode = treePolicy(rootNode, explorationParameter);
        StateT terminalState = getTerminalStateFromDefaultPolicy(selectedChildNode, agentInvoking);
        backPropagate(selectedChildNode, terminalState);
    }

    private MctsTreeNode<StateT, ActionT, AgentT> treePolicy(MctsTreeNode<StateT, ActionT, AgentT> node,
                                                             double explorationParameter) {
        while (!node.representsTerminalState()) {
            if (!node.isFullyExpanded())
                return expand(node);
            else
                node = getNodesBestChild(node, explorationParameter);
        }
        return node;
    }

    private MctsTreeNode<StateT, ActionT, AgentT> expand(MctsTreeNode<StateT, ActionT, AgentT> node) {
        ActionT randomUntriedAction = getRandomActionFromNodesUntriedActions(node);
        return node.addNewChildFromAction(randomUntriedAction);
    }

    private ActionT getRandomActionFromNodesUntriedActions(MctsTreeNode<StateT, ActionT, AgentT> node) {
        List<ActionT> untriedActions = node.getUntriedActionsForCurrentAgent();
        Collections.shuffle(untriedActions);
        return untriedActions.get(0);
    }

    private MctsTreeNode<StateT, ActionT, AgentT> getNodesBestChild(MctsTreeNode<StateT, ActionT, AgentT> node,
                                                                    double explorationParameter) {
        validateBestChildComputable(node);
        return getNodesBestChildConfidentlyWithExploration(node, explorationParameter);
    }

    private void validateBestChildComputable(MctsTreeNode<StateT, ActionT, AgentT> node) {
        if (!node.isFullyExpanded())
            throw new UnsupportedOperationException("Error: operation not supported if node not fully expanded");
        else if (node.hasUnvisitedChild())
            throw new UnsupportedOperationException(
                    "Error: operation not supported if node contains an unvisited child");
    }

    private ActionT getNodesMostPromisingAction(MctsTreeNode<StateT, ActionT, AgentT> node) {
        validateBestChildComputable(node);
        MctsTreeNode<StateT, ActionT, AgentT> bestChildWithoutExploration =
                getNodesBestChildConfidentlyWithExploration(node, NO_EXPLORATION);
        return bestChildWithoutExploration.getIncomingAction();
    }

    private MctsTreeNode<StateT, ActionT, AgentT> getNodesBestChildConfidentlyWithExploration(
            MctsTreeNode<StateT, ActionT, AgentT> node, double explorationParameter) {
        return node.getChildNodes().stream()
                .max((node1, node2) -> Double.compare(
                        calculateUctValue(node1, explorationParameter),
                        calculateUctValue(node2, explorationParameter))).get();
    }

    private double calculateUctValue(MctsTreeNode<StateT, ActionT, AgentT> node, double explorationParameter) {
        return node.getDomainTheoreticValue()
                + explorationParameter
                * (Math.sqrt((2 * Math.log(node.getParentsVisitCount())) / node.getVisitCount()));
    }

    private StateT getTerminalStateFromDefaultPolicy(
            MctsTreeNode<StateT, ActionT, AgentT> node, AgentT agentInvoking) {
        StateT nodesStateClone = node.getDeepCloneOfRepresentedState();
        return agentInvoking.getTerminalStateByPerformingSimulationFromState(nodesStateClone);
    }

    private void backPropagate(MctsTreeNode<StateT, ActionT, AgentT> node, StateT terminalState) {
        while (node != null) {
            double reward = calculateStatesRewardForNode(terminalState, node);
            node.updateDomainTheoreticValue(reward);
            node = node.getParentNode();
        }
    }

    private double calculateStatesRewardForNode(StateT terminalState, MctsTreeNode<StateT, ActionT, AgentT> node) {
        // todo: don't violate the law of Demeter
        AgentT parentsStatesCurrentAgent = node.getRepresentedStatesPreviousAgent();
        return parentsStatesCurrentAgent.getRewardFromTerminalState(terminalState);
    }

    public void dontClone(final Class<?>... classes) {
        cloner.dontClone(classes);
    }
}