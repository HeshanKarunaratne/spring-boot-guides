package com.example.springbootfeatures.config;

import com.example.springbootfeatures.aspect.PerformanceTrackerHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * @author Heshan Karunaratne
 */
@Configurable
public class ObservationAspectConfig {

    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        observationRegistry.observationConfig().observationHandler(new PerformanceTrackerHandler());
        return new ObservedAspect(observationRegistry);
    }
}
