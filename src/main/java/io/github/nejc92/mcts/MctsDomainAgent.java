package io.github.nejc92.mcts;

public interface MctsDomainAgent<StateT extends MctsDomainState> {

    StateT getTerminalStateByPerformingSimulationFromState(StateT state);
    double getRewardFromTerminalState(StateT terminalState);
}