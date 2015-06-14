package io.github.nejc92.mcts;

public class TestAgent implements MctsDomainAgent<TestState> {

    @Override
    public double getSimulationResult(TestState nonTerminalState) {
        return 0.0;
    }
}