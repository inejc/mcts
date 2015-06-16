package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;

public class Mcts<StateT extends MctsDomainState<ActionT>, ActionT> {

    private int numberOfIterations;
    private double explorationParameter;
    private Cloner cloner;

    public Mcts(int numberOfIterations, double explorationParameter) {
        this.numberOfIterations = numberOfIterations;
        this.explorationParameter = explorationParameter;
        cloner = new Cloner();
    }

    public ActionT uctSearch(StateT state, double explorationParameter) {
        MctsTreeNode<StateT, ActionT> rootNode = createRootNode(state);
        for(int i = 0; i < numberOfIterations; i++) {
            performOneMctsIteration(rootNode);
        }
        return null;
    }

    private MctsTreeNode<StateT, ActionT> createRootNode(StateT state) {
        return new MctsTreeNode<>(state, explorationParameter, cloner);
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