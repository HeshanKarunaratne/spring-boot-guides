package com.example.springbatch1.listener;

import com.example.springbatch1.entity.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;

/**
 * @author Heshan Karunaratne
 */
@Slf4j
public class StepSkipListener implements SkipListener<Customer, Number> {
    // Item Reader
    @Override
    public void onSkipInRead(Throwable t) {
        log.info("A Failure on Read, Error: {}", t.getMessage());
    }

    // Item Writer
    @Override
    public void onSkipInWrite(Number item, Throwable t) {
        log.info("A Failure on Write, Error: {}, {}", t.getMessage(), item);
    }

    // Item Processor
    @SneakyThrows
    @Override
    public void onSkipInProcess(Customer item, Throwable t) {
        log.info("Item {}, Error: {}", new ObjectMapper().writeValueAsString(item), t.getMessage());
    }
}
