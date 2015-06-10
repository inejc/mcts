package io.github.nejc92.mcts;

public class Mcts<T, S extends MctsDomainState<T>> {
    public void test(S state){
        MctsTreeNode<T, S> node = new MctsTreeNode<>(state);
        T action = node.getStatesIncomingAction();
        System.out.print(action);
    }
}