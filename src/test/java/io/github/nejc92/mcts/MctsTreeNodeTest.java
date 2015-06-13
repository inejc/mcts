package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class MctsTreeNodeTest {

    private List<String> allPossibleActions;
    private List<String> availableActions;
    private MctsTreeNode<MctsTreeNodeTestState, String> node;
    private MctsTreeNodeTestState state;
    private Cloner cloner = new Cloner();

    @Before
    public void setUp() {
        allPossibleActions = new ArrayList<>();
        allPossibleActions.add("action0");
        allPossibleActions.add("action1");
        allPossibleActions.add("action2");
        availableActions = allPossibleActions.subList(0, 2);
        state = new MctsTreeNodeTestState(availableActions);
        node = new MctsTreeNode<>(state, cloner);
    }

    @Test
    public void testUpdateDomainTheoreticValue() {
        node.updateDomainTheoreticValue(0.3);
        assertEquals(0.3, node.getTotalReward(), 0.001);
        assertEquals(1, node.getVisitCount());
    }

    @Test
    public void testDeepCloneRepresentedState() {
        MctsTreeNodeTestState clone = node.deepCloneRepresentedState();
        assertNotEquals(state, clone);
    }

    @Test
    public void testAddNewChildFromValidAction() {
        MctsTreeNode<MctsTreeNodeTestState, String> child = node.addNewChildFromAction(availableActions.get(0));
        assertEquals(availableActions.get(0), child.getIncomingAction());
        assertEquals(node, child.getParent());
        availableActions.remove(0);
        assertEquals(availableActions, node.getUntriedActionsForRepresentedStatesCurrentPlayer());
    }

    @Test(expected= IllegalArgumentException.class)
    public void testAddNewChildFromInvalidAction() {
        MctsTreeNode<MctsTreeNodeTestState, String> child = node.addNewChildFromAction(allPossibleActions.get(2));
    }

    @Test(expected= IllegalArgumentException.class)
    public void testAddNewChildFromTriedAction() {
        MctsTreeNode<MctsTreeNodeTestState, String> child0 = node.addNewChildFromAction(availableActions.get(0));
        MctsTreeNode<MctsTreeNodeTestState, String> child1 = node.addNewChildFromAction(availableActions.get(0));
    }
}