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

        try {


            // sth like a factory
            if (type == TaskType.CLOSE_ENROLLMENT) {
                CloseEnrollmentTask task = new CloseEnrollmentTask(this, shareLinkService);
                ScheduledFuture<?> scheduledTask = oneTimeTaskScheduler.schedule(task, triggerDate);
                tasks.put(TaskType.CLOSE_ENROLLMENT, scheduledTask);
            }

        }
        catch (Exception e) {
            System.out.println("Put");
        }
    }

    public void cancelCurrent(TaskType type) {
        try {


            if (isScheduled(type)) {
                tasks.get(type).cancel(false);
                tasks.remove(type);
            }
        }
        catch (Exception e) {
            System.out.println("cancelCurrent");
        }

    }


    public void remove(TaskType type) {
        try {
            if (isScheduled(type))
                tasks.remove(type);
        }
        catch (Exception e) {
            System.out.println("Remove");
        }
    }

    public void removeAll() {
        try {


            tasks.forEach((k, v) -> {
                v.cancel(true);
            });
            tasks.clear();
        }
        catch (Exception e) {
            System.out.println("removeAll");
        }
    }

    public boolean isScheduled(TaskType type) {
        boolean ret = false;
        try {
            ret = tasks.containsKey(type);
            return tasks.containsKey(type);
        }
        catch (Exception e) {
            System.out.println("isScheduled");
        }
        return ret;
    }


}
