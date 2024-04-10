package com.company.project.schedulers;

import com.company.project.service.ShareLinkService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;

public class ScheduledTasks {
    private final HashMap<TaskType, ScheduledFuture<?>> tasks;
    private final ThreadPoolTaskScheduler oneTimeTaskScheduler;

    public ScheduledTasks(ThreadPoolTaskScheduler oneTimeTaskScheduler) {
        tasks = new HashMap<>();
        this.oneTimeTaskScheduler = oneTimeTaskScheduler;
    }


    // TODO nie da się przez jebany shareLinkService który musi mieć EnrollmentService
    public void put(TaskType type, Instant triggerDate, ShareLinkService shareLinkService) {

        // sth like a factory
        if (type == TaskType.CLOSE_ENROLLMENT) {
            CloseEnrollmentTask task = new CloseEnrollmentTask(this, shareLinkService);
            ScheduledFuture<?> scheduledTask = oneTimeTaskScheduler.schedule(task, triggerDate);
            tasks.put(TaskType.CLOSE_ENROLLMENT, scheduledTask);
        }

    }

    public void cancelCurrent(TaskType type) {
        if (isScheduled(type)) {
            tasks.get(type).cancel(false);
            tasks.remove(type);
        }

    }


    public void remove(TaskType type) {
        if (isScheduled(type))
            tasks.remove(type);
    }

    public boolean isScheduled(TaskType type) {
        return tasks.containsKey(type);
    }


}
