package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;

import java.util.Collections;
import java.util.List;

public class Mcts<StateT extends MctsDomainState<ActionT, AgentT>, ActionT, AgentT extends MctsDomainAgent<StateT>> {

    private final int numberOfIterations;
    private final Cloner cloner;

    public static<StateT extends MctsDomainState<ActionT, AgentT>, ActionT, AgentT extends MctsDomainAgent<StateT>>
        Mcts<StateT, ActionT, AgentT> initialize(int numberOfIterations) {
            Cloner cloner = new Cloner();
            return new Mcts<>(numberOfIterations, cloner);
    }

    private Mcts(int numberOfIterations, Cloner cloner) {
        this.numberOfIterations = numberOfIterations;
        this.cloner = cloner;
    }

    public ActionT uctSearch(StateT state, double explorationParameter) {
        MctsTreeNode<StateT, ActionT, AgentT> rootNode = new MctsTreeNode<>(state, explorationParameter, cloner);
        for (int i = 0; i < numberOfIterations; i++) {
            performMctsIteration(rootNode, state.getCurrentAgent());
        }
        return rootNode.getMostPromisingAction();
    }

    private void performMctsIteration(MctsTreeNode<StateT, ActionT, AgentT> rootNode, AgentT agentInvoking) {
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
        return untriedActions.get(0);
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
        // todo: don't violate the law of Demeter
        AgentT parentsStatesAgent = treeNode.getRepresentedStatesPreviousAgent();
        return parentsStatesAgent.getRewardFromTerminalState(terminalState);
    }

    public void dontClone(final Class<?>... classes) {
        cloner.dontClone(classes);
    }
}