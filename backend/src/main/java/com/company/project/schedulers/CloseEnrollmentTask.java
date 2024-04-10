package com.company.project.schedulers;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CloseEnrollmentTask implements Runnable {
    private ScheduledTasks scheduledTasks;

    @Override
    public void run() {
        System.out.println("Hello there");
        scheduledTasks.remove(TaskType.CLOSE_ENROLLMENT);
    }

    @Override
    public String toString() {
        return "CloseEnrollmentTask [type=" + "]";
    }

}
