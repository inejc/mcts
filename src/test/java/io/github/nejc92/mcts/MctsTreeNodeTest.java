package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MctsTreeNodeTest {

    private static final double EXPLORATION_PARAMETER = 0.6;

    private List<String> allPossibleActions;
    private List<String> availableActions;
    private MctsTreeNode<TestState, String> rootNode;
    private TestState state;
    private Cloner cloner = new Cloner();

    @Before
    public void setUp() {
        allPossibleActions = new ArrayList<>(Arrays.asList("0", "1", "2"));
        availableActions = allPossibleActions.subList(0, 2);
        state = new TestState(availableActions);
        rootNode = new MctsTreeNode<>(state, EXPLORATION_PARAMETER, cloner);
    }

    @Test
    public void testDeepCloneRepresentedState() {
        TestState clone = rootNode.deepCloneRepresentedState();
        assertNotEquals(state, clone);
    }

    @Test
    public void isRootNode() {
        MctsTreeNode<TestState, String> child = rootNode.addNewChildFromAction(allPossibleActions.get(0));
        assertTrue(rootNode.isRootNode());
        assertFalse(child.isRootNode());
    }

    @Test
    public void testGetUntriedActionsForCurrentPlayer() {
        assertEquals(availableActions, rootNode.getUntriedActionsForCurrentAgent());
        rootNode.addNewChildFromAction(availableActions.get(0));
        availableActions.remove(0);
        assertEquals(availableActions, rootNode.getUntriedActionsForCurrentAgent());
        rootNode.addNewChildFromAction(availableActions.get(0));
        assertEquals(new ArrayList<String>(), rootNode.getUntriedActionsForCurrentAgent());
    }

    @Test
    public void testAddNewChildFromValidAction() {
        MctsTreeNode<TestState, String> child = rootNode.addNewChildFromAction(allPossibleActions.get(0));
        assertEquals(rootNode, child.getParentNode());
    }

    @Test(expected= IllegalArgumentException.class)
    public void testAddNewChildFromInvalidAction() {
        rootNode.addNewChildFromAction(allPossibleActions.get(2));
    }

    @Test(expected= IllegalArgumentException.class)
    public void testAddNewChildFromTriedAction() {
        rootNode.addNewChildFromAction(availableActions.get(0));
        rootNode.addNewChildFromAction(availableActions.get(0));
    }

    @Test
    public void testIsFullyExpanded() {
        assertFalse(rootNode.isFullyExpanded());
        rootNode.addNewChildFromAction(availableActions.get(0));
        assertFalse(rootNode.isFullyExpanded());
        rootNode.addNewChildFromAction(availableActions.get(1));
        assertTrue(rootNode.isFullyExpanded());
    }

    @Test
    public void testGetBestChildWithLotsOfVisits() {
        MctsTreeNode<TestState, String> child0 = rootNode.addNewChildFromAction(availableActions.get(0));
        MctsTreeNode<TestState, String> child1 = rootNode.addNewChildFromAction(availableActions.get(1));
        for (int i = 0; i < 21; i++) {
            rootNode.updateDomainTheoreticValue(0);
            if (i < 20) child0.updateDomainTheoreticValue(0.25); // totalReward: 5 visitCount: 20
            else child1.updateDomainTheoreticValue(0.5);         // totalReward: 0.5 visitCount: 1
        }
        assertEquals(child1, rootNode.getBestChild());
    }

    @Test
    public void testGetBestChildWithSameVisits() {
        MctsTreeNode<TestState, String> child0 = rootNode.addNewChildFromAction(availableActions.get(0));
        MctsTreeNode<TestState, String> child1 = rootNode.addNewChildFromAction(availableActions.get(1));
        for (int i = 0; i < 21; i++) {
            rootNode.updateDomainTheoreticValue(0);
            child0.updateDomainTheoreticValue(0.01); // totalReward: 0.2 visitCount: 20
            child1.updateDomainTheoreticValue(0.02); // totalReward: 0.4 visitCount: 20
        }
        assertEquals(child1, rootNode.getBestChild());
    }

    @Test(expected= UnsupportedOperationException.class)
    public void testGetBestChildWithUnvisitedChildNodes() {
        MctsTreeNode<TestState, String> child0 = rootNode.addNewChildFromAction(availableActions.get(0));
        assertEquals(child0, rootNode.getBestChild());
    }

    @Test(expected= UnsupportedOperationException.class)
    public void testGetBestChildWithoutChildNodes() {
        rootNode.getBestChild();
    }
}