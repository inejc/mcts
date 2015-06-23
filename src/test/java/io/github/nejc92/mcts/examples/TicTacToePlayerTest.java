//package io.github.nejc92.mcts.examples;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class TicTacToePlayerTest {
//
//    private static final char[][] DRAW_BOARD = new char[][] {
//        {'X', 'O', 'X'},
//        {'X', 'O', 'X'},
//        {'O', 'X', 'O'}
//    };
//
//    private static final char[][] NOUGHT_WON_BOARD = new char[][] {
//        {'O', '-', '-'},
//        {'X', 'O', 'X'},
//        {'-', '-', 'O'}
//    };
//
//    private static final double WIN_REWARD = 1;
//    private static final double DRAW_REWARD = 0.5;
//    private static final double LOSE_REWARD = 0;
//
//    private TicTacToePlayer crossPlayer = new TicTacToePlayer('X');
//    private TicTacToePlayer noughtPlayer = new TicTacToePlayer('O');
//    private TicTacToeState state;
//
//    @Before
//    public void setUp() {
//        state = new TicTacToeState(noughtPlayer, crossPlayer);
//    }
//
//    @Test
//    public void testSimulationToTerminalState() {
//        assertFalse(state.isTerminal());
//        crossPlayer.getRewardByPerformingSimulationFromState(state);
//        assertTrue(state.isTerminal());
//    }
//
//    @Test
//    public void testGetRewardFromTerminalStateWin() {
//        state.setBoard(NOUGHT_WON_BOARD);
//        double reward = noughtPlayer.getRewardByPerformingSimulationFromState(state);
//        assertEquals(WIN_REWARD, reward, 0);
//    }
//
//    @Test
//    public void testGetRewardFromTerminalStateLose() {
//        state.setBoard(NOUGHT_WON_BOARD);
//        double reward = crossPlayer.getRewardByPerformingSimulationFromState(state);
//        assertEquals(LOSE_REWARD, reward, 0);
//    }
//
//    @Test
//    public void testGetRewardFromTerminalStateDraw() {
//        state.setBoard(DRAW_BOARD);
//        double reward = crossPlayer.getRewardByPerformingSimulationFromState(state);
//        assertEquals(DRAW_REWARD, reward, 0);
//    }
//}