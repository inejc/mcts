package io.github.nejc92.mcts;

public class Mcts<DomainStateT extends MctsDomainState<DomainActionT>, DomainActionT> {
    public void test(DomainStateT state){
        MctsTreeNode<DomainStateT, DomainActionT> node = new MctsTreeNode<>(state);
    }
}