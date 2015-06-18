package io.github.nejc92.mcts.examples;

import io.github.nejc92.mcts.MctsDefaultPolicy;

public class TicTacToeRandomPlayout implements MctsDefaultPolicy<TicTacToeState> {

    @Override
    public double performPlayoutFromState(TicTacToeState state) {
        return 0;
    }
}