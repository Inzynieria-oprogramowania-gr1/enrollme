package com.company.project.configuration;


import com.company.project.entity.EnrolmentState;
import com.company.project.exception.implementations.ConflictException;
import com.company.project.repository.EnrollmentRepository;
import com.company.project.schedulers.ScheduledTasks;
import com.company.project.schedulers.TaskType;
import com.company.project.service.ShareLinkService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@AllArgsConstructor
@Component
public class EventListeners {

    private final EnrollmentRepository enrollmentRepository;
    private final ScheduledTasks scheduledTasks;
    private final ShareLinkService shareLinkService;

    // in  case of restarting application in production, we won't like to miss the deadline
    @EventListener(ApplicationReadyEvent.class)
    public void runTimersOnStartup() {
        enrollmentRepository
                .findAll()
                .stream()
                .findFirst()
                .ifPresent(enrollment -> {
                    LocalDateTime deadline = enrollment.getDeadline();
                    if (deadline != null && deadline.isAfter(LocalDateTime.now())) {
                        Instant instant = deadline.atZone(ZoneId.of("Europe/Warsaw")).toInstant();
                        scheduledTasks.put(TaskType.CLOSE_ENROLLMENT, instant, shareLinkService);

                    } else if (deadline != null) {
                        try {
                            shareLinkService.updateShareLink(EnrolmentState.CALCULATING);
                        } catch (ConflictException e) {
                            scheduledTasks.cancelCurrent(TaskType.CLOSE_ENROLLMENT);
                        } finally {
                            scheduledTasks.remove(TaskType.CLOSE_ENROLLMENT);
                        }
                    }

                });
    }
}


