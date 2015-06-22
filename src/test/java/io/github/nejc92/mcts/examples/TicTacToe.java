package io.github.nejc92.mcts.examples;

import io.github.nejc92.mcts.Mcts;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TicTacToe {

    private static final int NUMBER_OF_ITERATIONS = 500;
    private static final double EXPLORATION_PARAMETER = 0.667;

    private static Scanner scanner = new Scanner(System.in);
    private static Mcts<String, TicTacToeState, TicTacToePlayer> mcts = new Mcts<>(NUMBER_OF_ITERATIONS);
    private static TicTacToePlayer crossPlayer = new TicTacToePlayer('X');
    private static TicTacToePlayer noughtPlayer = new TicTacToePlayer('O');
    private static TicTacToePlayer firstPlayer = crossPlayer;
    private static TicTacToePlayer secondPlayer = noughtPlayer;
    //private static TicTacToePlayer firstPlayer = noughtPlayer; //test
    //private static TicTacToePlayer secondPlayer = crossPlayer; //test
    private static TicTacToeState state;

    private static final char[][] BOARD = new char[][] {
            {'O', 'X', 'O'},
            {'-', 'O', '-'},
            {'X', '-', 'X'}
    };

    public static void main(String... args) {
        while (true) {
            state = new TicTacToeState(firstPlayer, secondPlayer);
            //state.setBoard(BOARD); //test
            playOneTicTacToeGame();
            printWins();
            switchPlayerOrder();
        }
    }

    private static void playOneTicTacToeGame() {
        while (!state.isTerminal()) {
            String nextAction;
            if (state.getCurrentPlayer().boardPositionMarker == 'O')
                nextAction = mcts.uctSearch(state, state.getCurrentPlayer(), EXPLORATION_PARAMETER);
            else {
                System.out.print("Action input: ");
                nextAction = scanner.nextLine();
                //List<String> a = state.getAvailableActionsForCurrentAgent();
                //Collections.shuffle(a);
                //nextAction = a.get(0);
            }
            System.out.println();
            System.out.println(state.getCurrentPlayer().boardPositionMarker + " player:");
            state.performActionForCurrentAgent(nextAction);
            state.printBoard();
        }
        if (state.specificPlayerWon(crossPlayer)) {
            System.out.println(crossPlayer.boardPositionMarker + " player won.");
            crossPlayer.numberOfWins++;
        }
        else if (state.specificPlayerWon(noughtPlayer)) {
            System.out.println(noughtPlayer.boardPositionMarker + " player won.");
            noughtPlayer.numberOfWins++;
        }
        else {
            System.out.println("Draw.");
            crossPlayer.numberOfDraws++;
        }
    }

    private static void printWins() {
        System.out.println(crossPlayer.boardPositionMarker + " player wins: " + crossPlayer.numberOfWins);
        System.out.println(noughtPlayer.boardPositionMarker + " player wins: " + noughtPlayer.numberOfWins);
        System.out.println("D player wins: " + crossPlayer.numberOfDraws);
    }

    private static void switchPlayerOrder() {
        if (firstPlayer == crossPlayer) {
            firstPlayer = noughtPlayer;
            secondPlayer = crossPlayer;
        }
        else {
            firstPlayer = crossPlayer;
            secondPlayer = noughtPlayer;
        }
    }
}