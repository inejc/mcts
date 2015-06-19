package io.github.nejc92.mcts.examples;

import io.github.nejc92.mcts.MctsDomainState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicTacToeState implements MctsDomainState<String> {

    private static final char NOUGHT_PLAYER = 'O';
    private static final char CROSS_PLAYER = 'X';
    private static final char EMPTY_BOARD_POSITION = '-';
    private static final int BOARD_ROWS = 3;
    private static final int BOARD_COLUMNS = 3;
    private static final int ACTION_ROW_POSITION = 0;
    private static final int ACTION_COLUMN_POSITION = 1;
    private static final String ACTION_PARSING_DELIMITER = "-";

    private char[][] board;
    private char currentPlayer;

    public TicTacToeState() {
        board = new char[BOARD_ROWS][BOARD_COLUMNS];
        initializeEmptyBoard();
        currentPlayer = NOUGHT_PLAYER;
    }

    private void initializeEmptyBoard() {
        for (int row = 0; row < BOARD_ROWS; row++) {
            Arrays.fill(board[row], EMPTY_BOARD_POSITION);
        }
    }

    protected void setBoard(char[][] board) {
        this.board = board;
    }

    protected char[][] getBoard() {
        return board;
    }

    @Override
    public boolean isTerminal() {
        return isDraw() || somePlayerWon();
    }

    protected boolean isDraw() {
        for (int row = 0; row < BOARD_ROWS; row++) {
            if (boardRowContainsEmptyPosition(board[row]))
                return false;
        }
        return true;
    }

    private boolean boardRowContainsEmptyPosition(char[] row) {
        for (int column = 0; column < BOARD_COLUMNS; column++) {
            if (row[column] == EMPTY_BOARD_POSITION)
                return true;
        }
        return false;
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
            if (board[row][0] == player && board[row][1] == player && board[row][2] == player)
                return true;
        }
        return false;
    }

    protected boolean boardContainsPlayersFullColumn(char player) {
        for (int column = 0; column < BOARD_COLUMNS; column++) {
            if (board[0][column] == player && board[1][column] == player && board[2][column] == player)
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
            if (board[i][BOARD_COLUMNS - 1 - i] != player)
                return false;
        }
        return true;
    }

    private boolean boardContainsPlayersFullDescendingDiagonal(char player) {
        for (int i = 0; i < BOARD_ROWS; i++) {
            if (board[i][i] != player)
                return false;
        }
        return true;
    }

    @Override
    public int getNumberOfAvailableActionsForCurrentAgent() {
        return getAvailableActionsForCurrentAgent().size();
    }

    @Override
    public List<String> getAvailableActionsForCurrentAgent() {
        List<String> availableActions = new ArrayList<>();
        for (int row = 0; row < BOARD_ROWS; row++) {
            List<String> availableActionsInRow = getAvailableActionsInBoardRow(board[row], row);
            availableActions.addAll(availableActionsInRow);
        }
        return availableActions;
    }

    private List<String> getAvailableActionsInBoardRow(char[] row, int rowIndex) {
        List<String> availableActionsInRow = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < BOARD_COLUMNS; columnIndex++) {
            if (row[columnIndex] == EMPTY_BOARD_POSITION) {
                String action = generateActionFromRowColumn(rowIndex, columnIndex);
                availableActionsInRow.add(action);
            }
        }
        return availableActionsInRow;
    }

    private String generateActionFromRowColumn(int row, int column) {
        return Integer.toString(row) + ACTION_PARSING_DELIMITER + Integer.toString(column);
    }

    @Override
    public MctsDomainState performActionForCurrentAgent(String action) {
        validateIsValidAction(action);
        applyActionOnBoard(action);
        selectNextPlayer();
        return this;
    }

    private void validateIsValidAction(String action) {
        if (!getAvailableActionsForCurrentAgent().contains(action)) {
            throw new IllegalArgumentException("Invalid action passed as function parameter");
        }
    }

    private void applyActionOnBoard(String action) {
        int row = getRowFromAction(action);
        int column = getColumnFromAction(action);
        board[row][column] = currentPlayer;
    }

    private int getRowFromAction(String action) {
        String row = action.split(ACTION_PARSING_DELIMITER)[ACTION_ROW_POSITION];
        return Integer.parseInt(row);
    }

    private int getColumnFromAction(String action) {
        String column = action.split(ACTION_PARSING_DELIMITER)[ACTION_COLUMN_POSITION];
        return Integer.parseInt(column);
    }

    private void selectNextPlayer() {
        switch (currentPlayer){
            case NOUGHT_PLAYER:
                currentPlayer = CROSS_PLAYER;
            case CROSS_PLAYER:
                currentPlayer = NOUGHT_PLAYER;
        }
    }
}