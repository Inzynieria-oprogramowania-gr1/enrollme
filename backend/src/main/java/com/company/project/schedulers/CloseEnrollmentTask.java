package com.company.project.schedulers;


import com.company.project.entity.EnrolmentState;
import com.company.project.exception.implementations.ConflictException;
import com.company.project.service.ShareLinkService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CloseEnrollmentTask implements Runnable {
    private final ScheduledTasks scheduledTasks;
    private final ShareLinkService shareLinkService;


    @Override
    public void run() {
        try {
            shareLinkService.updateShareLink(EnrolmentState.CALCULATING);
        } catch (ConflictException e) {
            scheduledTasks.cancelCurrent(TaskType.CLOSE_ENROLLMENT);
        } finally {
            scheduledTasks.remove(TaskType.CLOSE_ENROLLMENT);
        }
    }

}
