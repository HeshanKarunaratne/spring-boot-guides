package com.example.springbootfeatures.aspect;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Heshan Karunaratne
 */
@Slf4j
public class PerformanceTrackerHandler implements ObservationHandler<Observation.Context> {
    @Override
    public void onStart(Observation.Context context) {
        log.info("execution started {}", context.getName());
        context.put("time", System.currentTimeMillis());
    }

    @Override
    public void onError(Observation.Context context) {
        log.error("Error occurred {}", context.getError().getMessage());
    }

    @Override
    public void onEvent(Observation.Event event, Observation.Context context) {

    }

    @Override
    public void onScopeOpened(Observation.Context context) {

    }

    @Override
    public void onScopeClosed(Observation.Context context) {

    }

    @Override
    public void onScopeReset(Observation.Context context) {

    }

    @Override
    public void onStop(Observation.Context context) {
        log.info("execution stopped " + context.getName() + " duration " + (System.currentTimeMillis() - context.getOrDefault("time", 0L)));
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return true;
    }
}
