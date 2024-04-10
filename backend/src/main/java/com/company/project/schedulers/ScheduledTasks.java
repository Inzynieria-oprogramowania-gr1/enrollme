package com.company.project.schedulers;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;

public class ScheduledTasks {
    private final HashMap<TaskType, ScheduledFuture<?>> tasks;
    private final ThreadPoolTaskScheduler taskScheduler;

    public ScheduledTasks(ThreadPoolTaskScheduler taskScheduler) {
        tasks = new HashMap<>();
        this.taskScheduler = taskScheduler;
    }


    public void put(TaskType type, Instant triggerDate) {

        // sth like a factory
        if(type == TaskType.CLOSE_ENROLLMENT)
        {
            CloseEnrollmentTask task = new CloseEnrollmentTask(this);
            ScheduledFuture<?> scheduledTask = taskScheduler.schedule(task, triggerDate);
            tasks.put(TaskType.CLOSE_ENROLLMENT, scheduledTask);
        }

    }

    public void cancelCurrent(TaskType type) {
        tasks.get(type).cancel(false);
        tasks.remove(type);
    }


    public void remove(TaskType type) {
        tasks.remove(type);
    }


}
