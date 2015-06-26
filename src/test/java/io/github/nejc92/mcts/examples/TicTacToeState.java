package io.github.nejc92.mcts.examples;

import io.github.nejc92.mcts.MctsDomainState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicTacToeState implements MctsDomainState<String, TicTacToePlayer> {

    private static final int BOARD_SIZE = 3;
    private static final char EMPTY_BOARD_POSITION = '-';
    private static final int ACTION_ROW_POSITION = 0;
    private static final int ACTION_COLUMN_POSITION = 1;
    private static final int FINAL_ROUND = 9;

    private char[][] board;
    private TicTacToePlayer[] players;
    private int currentPlayerIndex;
    private int previousPlayerIndex;
    private int currentRound;

    public TicTacToeState(TicTacToePlayer.Type playerToBegin) {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        initializeEmptyBoard();
        initializePlayers();
        setPlayerToBegin(playerToBegin);
        currentRound = 0;
    }

    private void initializeEmptyBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            Arrays.fill(board[row], EMPTY_BOARD_POSITION);
        }
    }

    private void initializePlayers() {
        players = new TicTacToePlayer[2];
        players[0] = new TicTacToePlayer('O');
        players[1] = new TicTacToePlayer('X');
    }

    private void setPlayerToBegin(TicTacToePlayer.Type playerToBegin) {
        switch (playerToBegin) {
            case NOUGHT:
                currentPlayerIndex = 0;
                previousPlayerIndex = 1;
                break;
            case CROSS:
                currentPlayerIndex = 1;
                previousPlayerIndex = 0;
        }
    }

    protected void setBoard(char[][] board) {
        this.board = board;
    }

    protected char[][] getBoard() {
        return board;
    }

    protected void setCurrentRound(int round) {
        this.currentRound = round;
    }

    @Override
    public boolean isTerminal() {
        return somePlayerWon() || isDraw();
    }

    public boolean isDraw() {
        return !somePlayerWon() && currentRound == FINAL_ROUND;
    }

    private boolean somePlayerWon() {
        return specificPlayerWon(players[currentPlayerIndex])
                || specificPlayerWon(players[previousPlayerIndex]);
    }

    protected boolean specificPlayerWon(TicTacToePlayer player) {
        return boardContainsPlayersFullRow(player)
                || boardContainsPlayersFullColumn(player)
                || boardContainsPlayersFullDiagonal(player);
    }

    private boolean boardContainsPlayersFullRow(TicTacToePlayer player) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (board[row][0] == player.boardPositionMarker
                    && board[row][1] == player.boardPositionMarker
                    && board[row][2] == player.boardPositionMarker)
                return true;
        }
        return false;
    }

    private boolean boardContainsPlayersFullColumn(TicTacToePlayer player) {
        for (int column = 0; column < BOARD_SIZE; column++) {
            if (board[0][column] == player.boardPositionMarker
                    && board[1][column] == player.boardPositionMarker
                    && board[2][column] == player.boardPositionMarker)
                return true;
        }
        return false;
    }

    private boolean boardContainsPlayersFullDiagonal(TicTacToePlayer player) {
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
    public TicTacToePlayer getCurrentAgent() {
        return players[currentPlayerIndex];
    }

    @Override
    public TicTacToePlayer getPreviousAgent() {
        return players[previousPlayerIndex];
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
                String action = generateActionFromBoardPosition(rowIndex, columnIndex);
                availableActionsInRow.add(action);
            }
        }
        return availableActionsInRow;
    }

    private String generateActionFromBoardPosition(int row, int column) {
        return Integer.toString(row) + Integer.toString(column);
    }

    @Override
    public MctsDomainState performActionForCurrentAgent(String action) {
        validateIsValidAction(action);
        applyActionOnBoard(action);
        selectNextPlayer();
        currentRound++;
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
        board[row][column] = players[currentPlayerIndex].boardPositionMarker;
    }

    protected MctsDomainState undoAction(String action) {
        validateIsValidUndoAction(action);
        applyUndoActionOnBoard(action);
        selectNextPlayer();
        currentRound--;
        return this;
    }

    private void validateIsValidUndoAction(String action) {
        int row = getRowFromAction(action);
        int column = getColumnFromAction(action);
        if (!(-1 < row && row < 3) && !(-1 < column && column < 3))
            throw new IllegalArgumentException("Invalid action passed as function parameter");
    }

    private void applyUndoActionOnBoard(String action) {
        int row = getRowFromAction(action);
        int column = getColumnFromAction(action);
        board[row][column] = EMPTY_BOARD_POSITION;
    }

    private int getRowFromAction(String action) {
        String row = action.split("")[ACTION_ROW_POSITION];
        return Integer.parseInt(row);
    }

    private int getColumnFromAction(String action) {
        String column = action.split("")[ACTION_COLUMN_POSITION];
        return Integer.parseInt(column);
    }

    private void selectNextPlayer() {
        currentPlayerIndex = 2 - currentPlayerIndex - 1;
        previousPlayerIndex = 2 - previousPlayerIndex - 1;
    }
}