package com.kotakotik.pondermaker.common;

public interface IDelegatedWithSteps<S extends IDelegatedWithSteps<S>> {
    Runnable getSteps();
    void setSteps(Runnable steps);

    default void runSteps() {
        getSteps().run();
    }

    default S step(Runnable step) {
        Runnable oldFunc = getSteps();
        setSteps(() -> {
            oldFunc.run();
            step.run();
        });
        return getSelf();
    }

    S getSelf();
}
