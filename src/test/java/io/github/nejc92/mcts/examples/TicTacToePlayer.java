package io.github.nejc92.mcts.examples;

import io.github.nejc92.mcts.MctsDomainAgent;

import java.util.Collections;
import java.util.List;

public class TicTacToePlayer implements MctsDomainAgent<TicTacToeState> {

    private static final int FIRST_RANDOM_ACTION = 0;

    public char boardPositionMarker;
    public int numberOfWins;
    public int numberOfDraws;

    public TicTacToePlayer(char boardPositionMarker) {
        this.boardPositionMarker = boardPositionMarker;
        this.numberOfWins = 0;
        this.numberOfDraws = 0;
    }

    @Override
    public double getRewardByPerformingSimulationFromState(TicTacToeState state) {
        TicTacToeState terminalState = performSimulationFromState(state);
        return getRewardFromTerminalState(terminalState);
    }

    private TicTacToeState performSimulationFromState(TicTacToeState state) {
        while (!state.isTerminal()) {
            String action = getBiasedOrRandomActionFromStatesAvailableActions(state);
            state.performActionForCurrentAgent(action);
        }
        return state;
    }

    private String getBiasedOrRandomActionFromStatesAvailableActions(TicTacToeState state) {
        List<String> availableActions = state.getAvailableActionsForCurrentAgent();
        for (String action : availableActions) {
            if (actionEndsGame(state, action))
                return action;
        }
        return getRandomActionFromActions(availableActions);
    }

    private boolean actionEndsGame(TicTacToeState state, String action) {
        state.performActionForCurrentAgent(action);
        boolean actionEndsGame = state.isTerminal();
        state.undoAction(action);
        return actionEndsGame;
    }

    private String getRandomActionFromActions(List<String> actions) {
        Collections.shuffle(actions);
        return actions.get(FIRST_RANDOM_ACTION);
    }

    private double getRewardFromTerminalState(TicTacToeState terminalState) {
        if (terminalState.specificPlayerWon(this))
            return 1;
        else if (terminalState.isDraw())
            return 0;
        else
            return -10;
    }
}