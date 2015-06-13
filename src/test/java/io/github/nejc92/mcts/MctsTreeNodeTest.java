package io.github.nejc92.mcts;

import com.rits.cloning.Cloner;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class MctsTreeNodeTest {

    private List<MctsTreeNodeTestAction> allPossibleActions;
    private List<MctsTreeNodeTestAction> availableActions;
    private MctsTreeNode<MctsTreeNodeTestState, MctsTreeNodeTestAction> node;
    private MctsTreeNodeTestState state;
    private Cloner cloner = new Cloner();

    @Before
    public void setUp() {
        allPossibleActions = new ArrayList<>();
        allPossibleActions.add(new MctsTreeNodeTestAction());
        allPossibleActions.add(new MctsTreeNodeTestAction());
        allPossibleActions.add(new MctsTreeNodeTestAction());
        availableActions = allPossibleActions.subList(0, 3);
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
        List<MctsTreeNodeTestAction> availableActions = node.getUntriedActionsForRepresentedStatesCurrentPlayer();
        node.addNewChildFromAction(availableActions.get(0));
    }

    @Test(expected= IllegalArgumentException.class)
    public void testAddNewChildFromInvalidAction() {

    }
}