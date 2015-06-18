package io.github.nejc92.mcts.implementations;

import io.github.nejc92.mcts.MctsDefaultPolicy;

public class RandomTicTacToePlayout implements MctsDefaultPolicy<TicTacToeState> {

    @Override
    public double performPlayoutFromState(TicTacToeState state) {
        return 0;
    }
}