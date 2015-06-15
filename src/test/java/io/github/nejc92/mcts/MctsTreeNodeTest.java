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
    private MctsTreeNode<TestState, String> node;
    private TestState state;
    private Cloner cloner = new Cloner();

    @Before
    public void setUp() {
        allPossibleActions = new ArrayList<>();
        allPossibleActions.add("0");
        allPossibleActions.add("1");
        allPossibleActions.add("2");
        availableActions = allPossibleActions.subList(0, 2);
        state = new TestState(availableActions);
        node = new MctsTreeNode<>(state, cloner);
    }

//    @Test
//    public void testUpdateDomainTheoreticValue() {
//        node.updateDomainTheoreticValue(0.3);
//        assertEquals(0.3, node.getTotalReward(), 0);
//        assertEquals(1, node.getVisitCount());
//        node.updateDomainTheoreticValue(0.2);
//        assertEquals(0.5, node.getTotalReward(), 0);
//        assertEquals(2, node.getVisitCount());
//    }

    @Test
    public void testDeepCloneRepresentedState() {
        TestState clone = node.deepCloneRepresentedState();
        assertNotEquals(state, clone);
    }

    @Test
    public void testAddNewChildFromValidAction() {
        MctsTreeNode<TestState, String> child = node.addNewChildFromAction(availableActions.get(0));
        assertEquals(availableActions.get(0), child.getIncomingAction());
        assertEquals(node, child.getParentNode());
    }

    @Test(expected= IllegalArgumentException.class)
    public void testAddNewChildFromInvalidAction() {
        node.addNewChildFromAction(allPossibleActions.get(2));
    }

    @Test(expected= IllegalArgumentException.class)
    public void testAddNewChildFromTriedAction() {
        node.addNewChildFromAction(availableActions.get(0));
        node.addNewChildFromAction(availableActions.get(0));
    }

    @Test
    public void testGetUntriedActionsForCurrentPlayer() {
        assertEquals(availableActions, node.getUntriedActionsForCurrentPlayer());
        node.addNewChildFromAction(availableActions.get(0));
        availableActions.remove(0);
        assertEquals(availableActions, node.getUntriedActionsForCurrentPlayer());
        node.addNewChildFromAction(availableActions.get(0));
        assertEquals(new ArrayList<String>(), node.getUntriedActionsForCurrentPlayer());
    }

    @Test
    public void testIsFullyExpanded() {
        assertFalse(node.isFullyExpanded());
        node.addNewChildFromAction(availableActions.get(0));
        assertFalse(node.isFullyExpanded());
        node.addNewChildFromAction(availableActions.get(1));
        assertTrue(node.isFullyExpanded());
    }

    @Test
    public void testIsRootNode() {
        assertTrue(node.isRootNode());
        MctsTreeNode<TestState, String> child = node.addNewChildFromAction(availableActions.get(0));
        assertFalse(child.isRootNode());
    }

    @Test(expected= UnsupportedOperationException.class)
    public void testGetParentNodeOnRoot() {
        node.getParentNode();
    }

    @Test(expected= UnsupportedOperationException.class)
    public void testGetIncomingActionOnRoot() {
        node.getIncomingAction();
    }
}