package io.github.nejc92.mcts;

public class Mcts<StateT extends MctsDomainState<ActionT>, ActionT> {

    private int numberOfIterations;

    public Mcts(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public ActionT uctSearch(StateT state, double explorationParameter) {
        MctsTreeNode<StateT, ActionT> rootNode = MctsTreeNode.createRootNode(state, explorationParameter);
        for(int i = 0; i < numberOfIterations; i++) {
            performOneMctsIteration(rootNode);
        }
        return null;
    }

    private void performOneMctsIteration(MctsTreeNode<StateT, ActionT> rootNode) {
        MctsTreeNode<StateT, ActionT> selectedChildNode = treePolicy(rootNode);
        double simulationResult = selectedChildNode.deepCloneRepresentedState().performSimulationForCurrentAgent();
        backPropagate(selectedChildNode, simulationResult);
    }

    private MctsTreeNode<StateT, ActionT> treePolicy(MctsTreeNode<StateT, ActionT> rootNode) {
        return rootNode;
    }

    private void backPropagate(MctsTreeNode<StateT, ActionT> treeNode, double simulationResult) {
    }
}