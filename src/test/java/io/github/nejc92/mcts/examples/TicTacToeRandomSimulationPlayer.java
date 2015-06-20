package io.github.nejc92.mcts.examples;

import io.github.nejc92.mcts.MctsDomainAgent;

import java.util.Collections;
import java.util.List;

public class TicTacToeRandomSimulationPlayer implements MctsDomainAgent<TicTacToeState> {

    private static final int FIRST_RANDOM_ACTION = 0;

    public char boardPositionMarker;

    public TicTacToeRandomSimulationPlayer(char boardPositionMarker) {
        this.boardPositionMarker = boardPositionMarker;
    }

    @Override
    public double getRewardByPerformingSimulationFromState(TicTacToeState state) {
        TicTacToeState terminalState = performRandomSimulationFromState(state);
        return getRewardFromTerminalState(terminalState);
    }

    private TicTacToeState performRandomSimulationFromState(TicTacToeState state) {
        while (!state.isTerminal()) {
            String randomAction = getRandomActionFromStatesAvailableActions(state);
            state.performActionForCurrentAgent(randomAction);
        }
        return state;
    }

    private String getRandomActionFromStatesAvailableActions(TicTacToeState state) {
        List<String> availableActions = state.getAvailableActionsForCurrentAgent();
        Collections.shuffle(availableActions);
        return availableActions.get(FIRST_RANDOM_ACTION);
    }

    private double getRewardFromTerminalState(TicTacToeState terminalState) {
        if (terminalState.specificPlayerWon(this))
            return 1;
        else if (terminalState.isDraw())
            return 0.5;
        else
            return 0;
    }
}