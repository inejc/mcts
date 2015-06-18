package io.github.nejc92.mcts.examples;

public class TicTacToeAction {
    public int row;
    public int column;

    public TicTacToeAction(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicTacToeAction that = (TicTacToeAction) o;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }
}