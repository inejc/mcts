package io.github.nejc92.mcts;

import java.util.Collections;
import java.util.List;

public class Mcts<StateT extends MctsDomainState<ActionT>, ActionT, DefaultPolicyT extends MctsDefaultPolicy<StateT>> {

    private int numberOfIterations;

    public Mcts(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public ActionT uctSearch(StateT state, double explorationParameter, DefaultPolicyT defaultPolicy) {
        MctsTreeNode<StateT, ActionT> rootNode = MctsTreeNode.createRootNode(state, explorationParameter);
        for (int i = 0; i < numberOfIterations; i++) {
            performOneMctsIteration(rootNode, defaultPolicy);
        }
        return rootNode.returnMostPromisingAction();
    }

    private void performOneMctsIteration(MctsTreeNode<StateT, ActionT> rootNode, DefaultPolicyT defaultPolicy) {
        MctsTreeNode<StateT, ActionT> selectedChildNode = treePolicy(rootNode);
        StateT selectedChildsStateClone = selectedChildNode.returnDeepCloneOfRepresentedState();
        double simulationResult = defaultPolicy.performPlayoutFromState(selectedChildsStateClone);
        backPropagate(selectedChildNode, simulationResult);
    }

    private MctsTreeNode<StateT, ActionT> treePolicy(MctsTreeNode<StateT, ActionT> treeNode) {
        while (!treeNode.representsTerminalState()) {
            if (!treeNode.isFullyExpanded())
                return expand(treeNode);
            else
                treeNode = treeNode.returnBestChild();
        }
        return treeNode;
    }

    private MctsTreeNode<StateT, ActionT> expand(MctsTreeNode<StateT, ActionT> treeNode) {
        List<ActionT> untriedActions = treeNode.returnUntriedActionsForCurrentAgent();
        Collections.shuffle(untriedActions);
        ActionT randomUntriedAction = untriedActions.get(0);
        return treeNode.addNewChildFromAction(randomUntriedAction);
    }

    private void backPropagate(MctsTreeNode<StateT, ActionT> treeNode, double simulationResult) {
        while (treeNode != null) {
            treeNode.updateDomainTheoreticValue(simulationResult);
            treeNode = treeNode.getParentNode();
        }
    }
}