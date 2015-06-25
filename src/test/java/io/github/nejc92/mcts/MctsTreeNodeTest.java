package io.github.nejc92.mcts;

import io.github.nejc92.mcts.examples.StaticState;
import io.github.nejc92.mcts.examples.TicTacToePlayer;
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
    private MctsTreeNode<StaticState, String, TicTacToePlayer> rootNode;
    private StaticState state;

    @Before
    public void setUp() {
        allPossibleActions = new ArrayList<>(Arrays.asList("0", "1", "2"));
        availableActions = allPossibleActions.subList(0, 2);
        state = new StaticState(availableActions);
        rootNode = MctsTreeNode.createRootNode(state, EXPLORATION_PARAMETER);
    }

    @Test
    public void testDeepCloneRepresentedState() {
        StaticState clone = rootNode.getDeepCloneOfRepresentedState();
        assertNotEquals(state, clone);
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
        MctsTreeNode<StaticState, String, TicTacToePlayer> child = rootNode.addNewChildFromAction(allPossibleActions.get(0));
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
    public void testGetBestChildWithLessVisitsLessReward() {
        MctsTreeNode<StaticState, String, TicTacToePlayer> child0 = rootNode.addNewChildFromAction(availableActions.get(0));
        MctsTreeNode<StaticState, String, TicTacToePlayer> child1 = rootNode.addNewChildFromAction(availableActions.get(1));
        for (int i = 0; i < 20; i++) {
            rootNode.updateDomainTheoreticValue(0);
            if (i < 5) child0.updateDomainTheoreticValue(0.1); // visitCount:  5, totalReward: 0.5
            else child1.updateDomainTheoreticValue(0.2);       // visitCount: 15, totalReward: 3
        }
        assertEquals(child0, rootNode.getBestChild());
    }

    @Test
    public void testGetBestChildWithLessVisitsMoreReward() {
        MctsTreeNode<StaticState, String, TicTacToePlayer> child0 = rootNode.addNewChildFromAction(availableActions.get(0));
        MctsTreeNode<StaticState, String, TicTacToePlayer> child1 = rootNode.addNewChildFromAction(availableActions.get(1));
        for (int i = 0; i < 20; i++) {
            rootNode.updateDomainTheoreticValue(0);
            if (i < 5) child0.updateDomainTheoreticValue(0.5); // visitCount:  5, totalReward: 2.5
            else child1.updateDomainTheoreticValue(0.1);       // visitCount: 15, totalReward: 1.5
        }
        assertEquals(child0, rootNode.getBestChild());
    }

    @Test
    public void testGetBestChildWithSameVisitsLessReward() {
        MctsTreeNode<StaticState, String, TicTacToePlayer> child0 = rootNode.addNewChildFromAction(availableActions.get(0));
        MctsTreeNode<StaticState, String, TicTacToePlayer> child1 = rootNode.addNewChildFromAction(availableActions.get(1));
        for (int i = 0; i < 21; i++) {
            rootNode.updateDomainTheoreticValue(0);
            child0.updateDomainTheoreticValue(0.01); // visitCount: 20, totalReward: 0.2
            child1.updateDomainTheoreticValue(0.02); // visitCount: 20, totalReward: 0.4
        }
        assertEquals(child1, rootNode.getBestChild());
    }

    @Test
    public void testGetMostPromisingAction() {
        MctsTreeNode<StaticState, String, TicTacToePlayer> child0 = rootNode.addNewChildFromAction(availableActions.get(0));
        MctsTreeNode<StaticState, String, TicTacToePlayer> child1 = rootNode.addNewChildFromAction(availableActions.get(1));
        for (int i = 0; i < 20; i++) {
            rootNode.updateDomainTheoreticValue(0);
            if (i < 3) child0.updateDomainTheoreticValue(0.1); // visitCount:  3, totalReward: 0.3
            else child1.updateDomainTheoreticValue(0.05);      // visitCount: 17, totalReward: 0.85
        }
        assertEquals(availableActions.get(0), rootNode.getMostPromisingAction());
    }

    @Test(expected= UnsupportedOperationException.class)
    public void testGetBestChildWithoutChildNodes() {
        rootNode.getBestChild();
    }

    @Test(expected= UnsupportedOperationException.class)
    public void testGetBestChildNotFullyExpanded() {
        rootNode.addNewChildFromAction(availableActions.get(0));
        rootNode.getBestChild();
    }

    @Test(expected= UnsupportedOperationException.class)
    public void testGetBestChildWithUnvisitedChildNodes() {
        rootNode.addNewChildFromAction(availableActions.get(0));
        rootNode.addNewChildFromAction(availableActions.get(1));
        rootNode.getBestChild();
    }
}