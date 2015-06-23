package io.github.nejc92.mcts.examples;

import io.github.nejc92.mcts.Mcts;

public class TicTacToe {

    static final int NUMBER_OF_ITERATIONS = 600;
    static final int NUMBER_OF_GAMES = 100;
    static final double EXPLORATION_PARAMETER = 0.4;

    static Mcts<String, TicTacToeState, TicTacToePlayer> mcts = new Mcts<>(NUMBER_OF_ITERATIONS);
    static TicTacToeState state;
    static int currentPlayerIndex = 1;
    static int draws = 0;

    public static void main(String... args) {
        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            playOneTicTacToeGame();
        }
        System.out.println("Draws: " + draws);
    }

    private static void playOneTicTacToeGame() {
        state = new TicTacToeState(currentPlayerIndex);
        while (!state.isTerminal()) {
            String nextAction = mcts.uctSearch(state, state.getCurrentPlayer(), EXPLORATION_PARAMETER);
            state.performActionForCurrentAgent(nextAction);
        }
        switchPlayerOrder();
        if (state.isDraw())
            draws++;
    }

    private static void switchPlayerOrder() {
        currentPlayerIndex = 2 - currentPlayerIndex - 1;
    }
}