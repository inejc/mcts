package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;

public class Mcts<StateT extends MctsDomainState<ActionT>, ActionT> {

    private int numberOfIterations;
    private Cloner cloner;

    public Mcts(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
        cloner = new Cloner();
    }

    public ActionT uctSearch(StateT state) {
        MctsTreeNode<StateT, ActionT> rootNode = new MctsTreeNode<>(state, cloner);
        for(int i=0; i<numberOfIterations; i++) {
            MctsTreeNode<StateT, ActionT> selectedChildNode = treePolicy(rootNode);
            double simulationResult = selectedChildNode.deepCloneRepresentedState().performSimulationForCurrentAgent();
            backPropagate(selectedChildNode, simulationResult);
        }
        return null;
    }

    private MctsTreeNode<StateT, ActionT> treePolicy(MctsTreeNode<StateT, ActionT> rootNode) {
        return rootNode;
    }

    private void backPropagate(MctsTreeNode<StateT, ActionT> treeNode, double simulationResult) {
    }
}