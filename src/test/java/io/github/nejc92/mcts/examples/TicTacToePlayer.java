package io.github.nejc92.mcts.examples;

import io.github.nejc92.mcts.MctsDomainAgent;

public class TicTacToePlayer implements MctsDomainAgent<TicTacToeState> {

    @Override
    public double getRewardByPerformingSimulationFromState(TicTacToeState state) {
        return 0;
    }
}