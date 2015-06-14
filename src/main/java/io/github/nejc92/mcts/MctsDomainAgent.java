package io.github.nejc92.mcts;

public interface MctsDomainAgent<StateT extends MctsDomainState> {

    double getSimulationResult(StateT nonTerminalState);
}