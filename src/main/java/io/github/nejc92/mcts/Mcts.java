package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;

public class Mcts<StateT extends MctsDomainState<AgentT, ActionT>, AgentT extends MctsDomainAgent<StateT>, ActionT> {

    private Cloner cloner;
    private int numberOfPlayouts;

    public Mcts(int numberOfPlayouts) {
        this.numberOfPlayouts = numberOfPlayouts;
        cloner = new Cloner();
    }

    public ActionT uctSearch(StateT state) {
        MctsTreeNode<StateT, AgentT, ActionT> rootNode = new MctsTreeNode<>(state, cloner);
        AgentT currentAgent = state.getCurrentAgent();
        for(int i=0; i<numberOfPlayouts; i++) {
            MctsTreeNode<StateT, AgentT, ActionT> selectedChildNode = treePolicy(rootNode);
            double simulationResult = currentAgent.getSimulationResult(selectedChildNode.deepCloneRepresentedState());
            backPropagate(selectedChildNode, simulationResult);
        }
        return null;
    }

    private MctsTreeNode<StateT, AgentT, ActionT> treePolicy(MctsTreeNode<StateT, AgentT, ActionT> rootNode) {
        return rootNode;
    }

    private void backPropagate(MctsTreeNode<StateT, AgentT, ActionT> treeNode, double simulationResult) {
    }
}