package io.github.nejc92.mcts.examples;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TicTacToeStateTest {

    private static final char[][] NON_TERMINAL_BOARD = new char[][] {
        {'X', '-', 'O'},
        {'-', 'O', '-'},
        {'X', 'X', '-'}
    };

    private static final char[][] DRAW_BOARD = new char[][] {
        {'X', 'O', 'X'},
        {'X', 'O', 'X'},
        {'O', 'X', 'O'}
    };

    private static final char[][] NOUGHT_WON_DIAGONAL_BOARD1 = new char[][] {
        {'O', '-', '-'},
        {'X', 'O', 'X'},
        {'-', '-', 'O'}
    };

    private static final char[][] NOUGHT_WON_DIAGONAL_BOARD2 = new char[][] {
        {'-', '-', 'O'},
        {'X', 'O', 'X'},
        {'O', '-', '-'}
    };

    private static final char[][] CROSS_WON_COLUMN_BOARD1 = new char[][] {
        {'X', 'O', '-'},
        {'X', 'O', '-'},
        {'X', '-', 'O'}
    };

    private static final char[][] CROSS_WON_COLUMN_BOARD2 = new char[][] {
        {'O', 'X', '-'},
        {'-', 'X', 'O'},
        {'-', 'X', 'O'}
    };

    private static final char[][] CROSS_WON_COLUMN_BOARD3 = new char[][] {
        {'O', 'O', 'X'},
        {'-', '-', 'X'},
        {'-', 'O', 'X'}
    };

    private static final char[][] CROSS_WON_ROW_BOARD1 = new char[][] {
        {'X', 'X', 'X'},
        {'O', '-', '-'},
        {'O', '-', 'O'}
    };

    private static final char[][] CROSS_WON_ROW_BOARD2 = new char[][] {
        {'-', 'O', '-'},
        {'X', 'X', 'X'},
        {'-', 'O', 'O'}
    };

    private static final char[][] CROSS_WON_ROW_BOARD3 = new char[][] {
        {'-', 'O', '-'},
        {'O', '-', '-'},
        {'X', 'X', 'X'}
    };

    private TicTacToeState state = new TicTacToeState();

    @Test
    public void testBoardContainsPlayersFullRow() {
        state.setBoard(CROSS_WON_ROW_BOARD1);
        assertTrue(state.boardContainsPlayersFullRow(state.getCrossPlayer()));
        state.setBoard(CROSS_WON_ROW_BOARD2);
        assertTrue(state.boardContainsPlayersFullRow(state.getCrossPlayer()));
        state.setBoard(CROSS_WON_ROW_BOARD3);
        assertTrue(state.boardContainsPlayersFullRow(state.getCrossPlayer()));
        state.setBoard(NON_TERMINAL_BOARD);
        assertFalse(state.boardContainsPlayersFullRow(state.getCrossPlayer()));
    }

    @Test
    public void testBoardContainsPlayersFullColumn() {
        state.setBoard(CROSS_WON_COLUMN_BOARD1);
        assertTrue(state.boardContainsPlayersFullColumn(state.getCrossPlayer()));
        state.setBoard(CROSS_WON_COLUMN_BOARD2);
        assertTrue(state.boardContainsPlayersFullColumn(state.getCrossPlayer()));
        state.setBoard(CROSS_WON_COLUMN_BOARD3);
        assertTrue(state.boardContainsPlayersFullColumn(state.getCrossPlayer()));
        state.setBoard(NON_TERMINAL_BOARD);
        assertFalse(state.boardContainsPlayersFullColumn(state.getCrossPlayer()));
    }

    @Test
    public void testBoardContainsPlayersFullDiagonal() {
        state.setBoard(NOUGHT_WON_DIAGONAL_BOARD1);
        assertTrue(state.boardContainsPlayersFullDiagonal(state.getNoughtPlayer()));
        state.setBoard(NOUGHT_WON_DIAGONAL_BOARD2);
        assertTrue(state.boardContainsPlayersFullDiagonal(state.getNoughtPlayer()));
        state.setBoard(NON_TERMINAL_BOARD);
        assertFalse(state.boardContainsPlayersFullDiagonal(state.getNoughtPlayer()));
    }

    @Test
    public void testIsDraw() {
        state.setBoard(NOUGHT_WON_DIAGONAL_BOARD2);
        assertFalse(state.isDraw());
        state.setBoard(NON_TERMINAL_BOARD);
        assertFalse(state.isDraw());
        state.setBoard(DRAW_BOARD);
        assertTrue(state.isDraw());
    }

    @Test
    public void testIsTerminal() {
        state.setBoard(NON_TERMINAL_BOARD);
        assertFalse(state.isTerminal());
        state.setBoard(NOUGHT_WON_DIAGONAL_BOARD2);
        assertTrue(state.isTerminal());
        state.setBoard(DRAW_BOARD);
        assertTrue(state.isTerminal());
    }

    @Test
    public void testGetAvailableActionsForCurrentAgent() {
        state.setBoard(NON_TERMINAL_BOARD);
        List<String> expectedAvailableActions = new ArrayList<>(Arrays.asList(
                "01", "10", "12", "22"
        ));
        assertEquals(expectedAvailableActions, state.getAvailableActionsForCurrentAgent());
    }

    @Test
    public void testPerformValidActionForCurrentAgent() {
        state.setBoard(NON_TERMINAL_BOARD);
        char[][] expectedBoard = new char[][] {
            {'X', 'O', 'O'},
            {'-', 'O', '-'},
            {'X', 'X', '-'}
        };
        state.performActionForCurrentAgent("01");
        assertArrayEquals(expectedBoard, state.getBoard());
    }

    @Test(expected= IllegalArgumentException.class)
    public void testPerformInvalidActionForCurrentAgent() {
        state.setBoard(NON_TERMINAL_BOARD);
        state.performActionForCurrentAgent("02");
    }
}