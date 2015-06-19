package io.github.nejc92.mcts;

public interface MctsDomainAgent<StateT extends MctsDomainState> {

    double getRewardByPerformingSimulationFromState(StateT state);
}