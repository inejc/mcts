package io.github.nejc92.mcts.examples;

import io.github.nejc92.mcts.MctsDomainState;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeState implements MctsDomainState<TicTacToeAction> {

    private static final char NOUGHT_PLAYER = 'O';
    private static final char CROSS_PLAYER = 'X';
    private static final char EMPTY_BOARD_POSITION = '-';
    private static final int BOARD_COLUMNS = 3;
    private static final int BOARD_ROWS = 3;

    private char[][] board;
    private char currentPlayer;

    public TicTacToeState() {
        board = new char[][] {
            {'-', '-', '-'},
            {'-', '-', '-'},
            {'-', '-', '-'}
        };
        currentPlayer = NOUGHT_PLAYER;
    }

    protected void setBoard(char[][] board) {
        this.board = board;
    }

    @Override
    public boolean isTerminal() {
        return isDraw() || somePlayerWon();
    }

    protected boolean isDraw() {
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int column = 0; column < BOARD_COLUMNS; column++) {
                if (board[row][column] == EMPTY_BOARD_POSITION)
                    return false;
            }
        }
        return true;
    }

    private boolean somePlayerWon() {
        return specificPlayerWon(NOUGHT_PLAYER) || specificPlayerWon(CROSS_PLAYER);
    }

    private boolean specificPlayerWon(char player) {
        return boardContainsPlayersFullRow(player) ||
               boardContainsPlayersFullColumn(player) ||
               boardContainsPlayersFullDiagonal(player);
    }

    protected boolean boardContainsPlayersFullRow(char player) {
        for (int row = 0; row < BOARD_ROWS; row++) {
            char position0 = board[row][0];
            char position1 = board[row][1];
            char position2 = board[row][2];
            if (position0 == player && position1 == player && position2 == player)
                return true;
        }
        return false;
    }

    protected boolean boardContainsPlayersFullColumn(char player) {
        for (int column = 0; column < BOARD_COLUMNS; column++) {
            char position0 = board[0][column];
            char position1 = board[1][column];
            char position2 = board[2][column];
            if (position0 == player && position1 == player && position2 == player)
                return true;
        }
        return false;
    }

    protected boolean boardContainsPlayersFullDiagonal(char player) {
        return boardContainsPlayersFullAscendingDiagonal(player) ||
               boardContainsPlayersFullDescendingDiagonal(player);
    }

    private boolean boardContainsPlayersFullAscendingDiagonal(char player) {
        for (int i = 0; i < BOARD_ROWS; i++) {
            if (board[i][i] != player)
                return false;
        }
        return true;
    }

    private boolean boardContainsPlayersFullDescendingDiagonal(char player) {
        for (int i = 0; i < BOARD_ROWS; i++) {
            if (board[i][BOARD_COLUMNS - 1 - i] != player)
                return false;
        }
        return true;
    }

    @Override
    public List<TicTacToeAction> getAvailableActionsForCurrentAgent() {
        List<TicTacToeAction> availableActions = new ArrayList<>();
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int column = 0; column < BOARD_COLUMNS; column++) {
                if (board[row][column] == EMPTY_BOARD_POSITION)
                    availableActions.add(new TicTacToeAction(row, column));
            }
        }
        return availableActions;
    }

    @Override
    public int getNumberOfAvailableActionsForCurrentAgent() {
        return getAvailableActionsForCurrentAgent().size();
    }

    @Override
    public MctsDomainState performActionForCurrentAgent(TicTacToeAction action) {
        if (getAvailableActionsForCurrentAgent().contains(action)) {
            board[action.row][action.column] = currentPlayer;
            selectNextPlayer();
            return this;
        }
        else
            throw new IllegalArgumentException("Invalid action passed as function parameter");
    }

    private void selectNextPlayer() {
        if (currentPlayer == NOUGHT_PLAYER)
            currentPlayer = CROSS_PLAYER;
        else
            currentPlayer = NOUGHT_PLAYER;
    }
}