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

    public static TicTacToeState initialize(TicTacToePlayer.Type playerToBegin) {
        char[][] board = initializeEmptyBoard();
        TicTacToePlayer[] players = initializePlayers();
        int currentPlayerIndex = getPlayerToBeginIndex(playerToBegin);
        return new TicTacToeState(board, players, currentPlayerIndex);
    }

    private TicTacToeState(char[][] board, TicTacToePlayer[] players, int currentPlayerIndex) {
        this.board = board;
        this.players = players;
        this.currentPlayerIndex = currentPlayerIndex;
        this.previousPlayerIndex = 2 - currentPlayerIndex - 1;
        this.currentRound = 0;
    }

    private static char[][] initializeEmptyBoard() {
        char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; row++) {
            Arrays.fill(board[row], EMPTY_BOARD_POSITION);
        }
        return board;
    }

    private static TicTacToePlayer[] initializePlayers() {
        TicTacToePlayer[] players = new TicTacToePlayer[2];
        players[0] = TicTacToePlayer.create(TicTacToePlayer.Type.NOUGHT);
        players[1] = TicTacToePlayer.create(TicTacToePlayer.Type.CROSS);
        return players;
    }

    private static int getPlayerToBeginIndex(TicTacToePlayer.Type playerToBegin) {
        switch (playerToBegin) {
            case NOUGHT:
                return 0;
            case CROSS:
                return 1;
            default:
                throw new IllegalArgumentException("Error: invalid player type passed as function parameter");
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
            if (board[row][0] == player.getBoardPositionMarker()
                    && board[row][1] == player.getBoardPositionMarker()
                    && board[row][2] == player.getBoardPositionMarker())
                return true;
        }
        return false;
    }

    private boolean boardContainsPlayersFullColumn(TicTacToePlayer player) {
        for (int column = 0; column < BOARD_SIZE; column++) {
            if (board[0][column] == player.getBoardPositionMarker()
                    && board[1][column] == player.getBoardPositionMarker()
                    && board[2][column] == player.getBoardPositionMarker())
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
            if (board[i][BOARD_SIZE - 1 - i] != player.getBoardPositionMarker())
                return false;
        }
        return true;
    }

    private boolean boardContainsPlayersFullDescendingDiagonal(TicTacToePlayer player) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][i] != player.getBoardPositionMarker())
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
            throw new IllegalArgumentException("Error: invalid action passed as function parameter");
        }
    }

    private void applyActionOnBoard(String action) {
        int row = getRowFromAction(action);
        int column = getColumnFromAction(action);
        board[row][column] = players[currentPlayerIndex].getBoardPositionMarker();
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
            throw new IllegalArgumentException("Error: invalid action passed as function parameter");
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

    @Override
    public MctsDomainState skipCurrentAgent() {
        return this;
    }
}