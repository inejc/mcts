package io.github.nejc92.mcts;

public class Mcts<StateT extends MctsDomainState<ActionT>, ActionT, DefaultPolicyT extends MctsDefaultPolicy<StateT>> {

    private int numberOfIterations;

    public Mcts(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public ActionT uctSearch(StateT state, double explorationParameter, DefaultPolicyT defaultPolicy) {
        MctsTreeNode<StateT, ActionT> rootNode = MctsTreeNode.createRootNode(state, explorationParameter);
        for(int i = 0; i < numberOfIterations; i++) {
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
        return treeNode;
    }

    private void backPropagate(MctsTreeNode<StateT, ActionT> treeNode, double simulationResult) {
    }
}