package io.github.nejc92.mcts.examples;

import io.github.nejc92.mcts.MctsDomainAgent;

public class TicTacToePlayer implements MctsDomainAgent<TicTacToeState> {

    public char boardPositionMarker;

    public TicTacToePlayer(char boardPositionMarker) {
        this.boardPositionMarker = boardPositionMarker;
    }

    @Override
    public double getRewardByPerformingSimulationFromState(TicTacToeState state) {
        TicTacToeState terminalState = performRandomSimulationFromState(state);
        return getRewardFromTerminalState(terminalState);
    }

    private TicTacToeState performRandomSimulationFromState(TicTacToeState state) {
        while (!state.isTerminal()) {

        }
        return state;
    }

    private double getRewardFromTerminalState(TicTacToeState terminalState) {
        return 0;
    }
}