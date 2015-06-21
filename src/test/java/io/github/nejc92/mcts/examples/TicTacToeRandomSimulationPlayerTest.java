package io.github.nejc92.mcts.examples;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TicTacToeRandomSimulationPlayerTest {

    private static final char[][] DRAW_BOARD = new char[][] {
            {'X', 'O', 'X'},
            {'X', 'O', 'X'},
            {'O', 'X', 'O'}
    };

    private static final char[][] NOUGHT_WON_BOARD = new char[][] {
            {'O', '-', '-'},
            {'X', 'O', 'X'},
            {'-', '-', 'O'}
    };

    private static final double WIN_REWARD = 1;
    private static final double DRAW_REWARD = 0.5;
    private static final double LOSE_REWARD = 0;

    private TicTacToeState state;

    @Before
    public void setUp() {
        state = new TicTacToeState();
    }

    @Test
    public void testSimulationToTerminalState() {
        assertFalse(state.isTerminal());
        TicTacToeRandomSimulationPlayer player = state.getCrossPlayer();
        player.getRewardByPerformingSimulationFromState(state);
        assertTrue(state.isTerminal());
    }

    @Test
    public void testGetRewardFromTerminalStateWin() {
        state.setBoard(NOUGHT_WON_BOARD);
        TicTacToeRandomSimulationPlayer noughtPlayer = state.getNoughtPlayer();
        double reward = noughtPlayer.getRewardByPerformingSimulationFromState(state);
        assertEquals(WIN_REWARD, reward, 0);
    }

    @Test
    public void testGetRewardFromTerminalStateLose() {
        state.setBoard(NOUGHT_WON_BOARD);
        TicTacToeRandomSimulationPlayer crossPlayer = state.getCrossPlayer();
        double reward = crossPlayer.getRewardByPerformingSimulationFromState(state);
        assertEquals(LOSE_REWARD, reward, 0);
    }

    @Test
    public void testGetRewardFromTerminalStateDraw() {
        state.setBoard(DRAW_BOARD);
        TicTacToeRandomSimulationPlayer crossPlayer = state.getCrossPlayer();
        double reward = crossPlayer.getRewardByPerformingSimulationFromState(state);
        assertEquals(DRAW_REWARD, reward, 0);
    }
}