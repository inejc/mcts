package io.github.nejc92.mcts;

public class MCTS<T, S extends DomainState<T>> {
    public void test(S state){
        TreeNode<T, S> node = new TreeNode<>(state);
        T action = node.getStatesIncomingAction();
        System.out.print(action);
    }
}