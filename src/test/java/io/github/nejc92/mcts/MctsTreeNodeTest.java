package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;
import io.github.nejc92.mcts.examples.StaticState;
import io.github.nejc92.mcts.examples.TicTacToePlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MctsTreeNodeTest {

    // private static final double EXPLORATION_PARAMETER = 0.6;

    private List<String> allPossibleActions;
    private List<String> availableActions;
    private MctsTreeNode<StaticState, String, TicTacToePlayer> rootNode;
    private StaticState state;
    private final Cloner cloner = new Cloner();

    @Before
    public void setUp() {
        allPossibleActions = new ArrayList<>(Arrays.asList("0", "1", "2"));
        availableActions = allPossibleActions.subList(0, 2);
        state = new StaticState(availableActions);
        rootNode = new MctsTreeNode<>(state, cloner);
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
        MctsTreeNode<StaticState, String, TicTacToePlayer> child =
                rootNode.addNewChildFromAction(allPossibleActions.get(0));
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
}