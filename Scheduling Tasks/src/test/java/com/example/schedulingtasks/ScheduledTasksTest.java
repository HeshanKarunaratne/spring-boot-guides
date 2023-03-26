package com.example.schedulingtasks;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ScheduledTasksTest {

    @Test
    public void reportCurrentTime() {
        ScheduledTasks scheduledtasks = Mockito.mock(ScheduledTasks.class);
        scheduledtasks.reportCurrentTime();
        Mockito.verify(scheduledtasks, Mockito.atLeast(1)).reportCurrentTime();
    }
}