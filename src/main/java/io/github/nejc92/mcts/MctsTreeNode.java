package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;
import java.util.List;
import java.util.ArrayList;

public class MctsTreeNode<DomainStateT extends MctsDomainState<DomainActionT>, DomainActionT> {

    private MctsTreeNode<DomainStateT, DomainActionT> parent;
    private List<MctsTreeNode<DomainStateT, DomainActionT>> children;
    private DomainStateT representedState;
    private DomainActionT incomingAction;
    private int visitCount;
    private double totalReward;

    //root node
    public MctsTreeNode(DomainStateT representedState) {
        this.children = new ArrayList<>();
        this.representedState = representedState;
        this.visitCount = 0;
        this.totalReward = 0.0;
    }

    //child node
    public MctsTreeNode(DomainStateT representedState, DomainActionT incomingAction, MctsTreeNode<DomainStateT, DomainActionT> parent) {
        this.children = new ArrayList<>();
        this.representedState = representedState;
        this.visitCount = 0;
        this.totalReward = 0.0;
        this.parent = parent;
        this.incomingAction = incomingAction;
    }

    public boolean isRootNode(){
        return parent == null;
    }

    public MctsTreeNode<DomainStateT, DomainActionT> getParent(){
        return parent;
    }

    public MctsTreeNode<DomainStateT, DomainActionT> addNewChild(DomainActionT childIncomingAction){
        DomainStateT stateClone = deepCloneRepresentedState();
        stateClone.performActionForCurrentPlayer(childIncomingAction);
        MctsTreeNode<DomainStateT, DomainActionT> child = new MctsTreeNode<>(stateClone, childIncomingAction, this);
        children.add(child);
        return child;
    }

    public DomainStateT deepCloneRepresentedState(){
        Cloner cloner = new Cloner();
        return cloner.deepClone(representedState);
    }

    public boolean representsTerminalState() {
        return representedState.isTerminal();
    }

    public List<DomainActionT> getUntriedActionsForRepresentedState(){
        List<DomainActionT> untriedActions = representedState.getAvailableActionsForCurrentPlayer();
        for(MctsTreeNode<DomainStateT, DomainActionT> child : children) {
            untriedActions.remove(child.getIncomingAction());
        }
        return untriedActions;
    }

    public DomainActionT getIncomingAction(){
        return incomingAction;
    }

    public int getVisitCount(){
        return visitCount;
    }

    public double getTotalReward(){
        return totalReward;
    }

    public boolean isFullyExpanded() {
        return representedState.getNumberOfAvailableActionsForCurrentPlayer() == children.size();
    }

    public void updateDomainTheoreticValue(double rewardAddend){
        visitCount += 1;
        totalReward += rewardAddend;
    }
}