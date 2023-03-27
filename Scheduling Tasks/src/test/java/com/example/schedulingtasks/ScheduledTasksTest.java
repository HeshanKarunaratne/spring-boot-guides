package com.example.schedulingtasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduledTasksTest {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Test
    public void reportCurrentTime() {
        ScheduledTasks scheduledtasks = Mockito.mock(ScheduledTasks.class);
        scheduledtasks.reportCurrentTime();
        Mockito.verify(scheduledtasks, Mockito.atLeast(1)).reportCurrentTime();
    }

    @Test
    public void reportCurrentTimeTest() throws InterruptedException {
        ScheduledTasks scheduledTasks = new ScheduledTasks();
        scheduledTasks.reportCurrentTime();
        Thread.sleep(5000);
        String currentTime = dateFormat.format(new Date());
        Assertions.assertEquals("The time is now " + currentTime, scheduledTasks.reportCurrentTime());
    }
}