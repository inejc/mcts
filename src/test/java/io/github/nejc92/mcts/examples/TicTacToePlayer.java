package io.github.nejc92.mcts.examples;

import io.github.nejc92.mcts.MctsDomainAgent;

import java.util.Collections;
import java.util.List;

public class TicTacToePlayer implements MctsDomainAgent<TicTacToeState> {

    private final char boardPositionMarker;

    public enum Type {
        NOUGHT, CROSS
    }

    public static TicTacToePlayer create(Type type) {
        switch (type) {
            case NOUGHT:
                return new TicTacToePlayer('O');
            case CROSS:
                return new TicTacToePlayer('X');
            default:
                throw new IllegalArgumentException("Error: invalid player type passed as function parameter");
        }
    }

    private TicTacToePlayer(char boardPositionMarker) {
        this.boardPositionMarker = boardPositionMarker;
    }

    public char getBoardPositionMarker() {
        return boardPositionMarker;
    }

    @Override
    public TicTacToeState getTerminalStateByPerformingSimulationFromState(TicTacToeState state) {
        while (!state.isTerminal()) {
            String action = getBiasedOrRandomActionFromStatesAvailableActions(state);
            state.performActionForCurrentAgent(action);
        }
        return state;
    }

    private String getBiasedOrRandomActionFromStatesAvailableActions(TicTacToeState state) {
        List<String> availableActions = state.getAvailableActionsForCurrentAgent();
        for (String action : availableActions) {
            if (actionWinsGame(state, action))
                return action;
        }
        return getRandomActionFromActions(availableActions);
    }

    private boolean actionWinsGame(TicTacToeState state, String action) {
        state.performActionForCurrentAgent(action);
        boolean actionEndsGame = state.isTerminal();
        state.undoAction(action);
        return actionEndsGame;
    }

    private String getRandomActionFromActions(List<String> actions) {
        Collections.shuffle(actions);
        return actions.get(0);
    }

    @Override
    public double getRewardFromTerminalState(TicTacToeState terminalState) {
        if (terminalState.specificPlayerWon(this))
            return 1;
        else if (terminalState.isDraw())
            return 0.5;
        else
            return 0;
    }
}