package io.github.nejc92.mcts.examples;

import io.github.nejc92.mcts.MctsDomainState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicTacToeState implements MctsDomainState<String> {

    private static final int BOARD_SIZE = 3;
    private static final char EMPTY_BOARD_POSITION = '-';
    private static final String ACTION_PARSING_DELIMITER = "-";
    private static final int ACTION_ROW_POSITION = 0;
    private static final int ACTION_COLUMN_POSITION = 1;

    private char[][] board;
    private TicTacToePlayer noughtPlayer;
    private TicTacToePlayer crossPlayer;
    private TicTacToePlayer currentPlayer;

    public TicTacToeState() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        initializeEmptyBoard();
        noughtPlayer = new TicTacToePlayer('O');
        crossPlayer = new TicTacToePlayer('X');
        currentPlayer = noughtPlayer;
    }

    private void initializeEmptyBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            Arrays.fill(board[row], EMPTY_BOARD_POSITION);
        }
    }

    protected void setBoard(char[][] board) {
        this.board = board;
    }

    protected char[][] getBoard() {
        return board;
    }

    protected TicTacToePlayer getNoughtPlayer() {
        return noughtPlayer;
    }

    protected TicTacToePlayer getCrossPlayer() {
        return crossPlayer;
    }

    @Override
    public boolean isTerminal() {
        return isDraw() || somePlayerWon();
    }

    protected boolean isDraw() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (boardRowContainsEmptyPosition(board[row]))
                return false;
        }
        return true;
    }

    private boolean boardRowContainsEmptyPosition(char[] row) {
        for (int column = 0; column < BOARD_SIZE; column++) {
            if (row[column] == EMPTY_BOARD_POSITION)
                return true;
        }
        return false;
    }

    private boolean somePlayerWon() {
        return specificPlayerWon(noughtPlayer) || specificPlayerWon(crossPlayer);
    }

    private boolean specificPlayerWon(TicTacToePlayer player) {
        return boardContainsPlayersFullRow(player)
                || boardContainsPlayersFullColumn(player)
                || boardContainsPlayersFullDiagonal(player);
    }

    protected boolean boardContainsPlayersFullRow(TicTacToePlayer player) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (board[row][0] == player.boardPositionMarker
                    && board[row][1] == player.boardPositionMarker
                    && board[row][2] == player.boardPositionMarker)
                return true;
        }
        return false;
    }

    protected boolean boardContainsPlayersFullColumn(TicTacToePlayer player) {
        for (int column = 0; column < BOARD_SIZE; column++) {
            if (board[0][column] == player.boardPositionMarker
                    && board[1][column] == player.boardPositionMarker
                    && board[2][column] == player.boardPositionMarker)
                return true;
        }
        return false;
    }

    protected boolean boardContainsPlayersFullDiagonal(TicTacToePlayer player) {
        return boardContainsPlayersFullAscendingDiagonal(player)
                || boardContainsPlayersFullDescendingDiagonal(player);
    }

    private boolean boardContainsPlayersFullAscendingDiagonal(TicTacToePlayer player) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][BOARD_SIZE - 1 - i] != player.boardPositionMarker)
                return false;
        }
        return true;
    }

    private boolean boardContainsPlayersFullDescendingDiagonal(TicTacToePlayer player) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][i] != player.boardPositionMarker)
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
        for (int row = 0; row < BOARD_SIZE; row++) {
            List<String> availableActionsInRow = getAvailableActionsInBoardRow(board[row], row);
            availableActions.addAll(availableActionsInRow);
        }
        return availableActions;
    }

    private List<String> getAvailableActionsInBoardRow(char[] row, int rowIndex) {
        List<String> availableActionsInRow = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < BOARD_SIZE; columnIndex++) {
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
        board[row][column] = currentPlayer.boardPositionMarker;
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
        if (currentPlayer == crossPlayer)
            currentPlayer = noughtPlayer;
        else
            currentPlayer = crossPlayer;
    }
}