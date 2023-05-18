package com.example.springbatch1.config;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

/**
 * @author Heshan Karunaratne
 */
public class ExceptionSkipPolicy implements SkipPolicy {
    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        return t instanceof NumberFormatException && skipCount == 2;
    }
}
