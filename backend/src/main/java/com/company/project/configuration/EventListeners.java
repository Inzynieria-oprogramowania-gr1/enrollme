package com.company.project.configuration;


import com.company.project.repository.EnrollmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@AllArgsConstructor
@Component
public class EventListeners {

    private final EnrollmentRepository enrollmentRepository;
    private final ThreadPoolTaskScheduler taskScheduler;

    // in  case of restarting application in production, we won't like to miss the deadline
    @EventListener(ApplicationReadyEvent.class)
    public void runTimersOnStartup() {
        enrollmentRepository
                .findAll()
                .stream()
                .findFirst()
                .ifPresent(enrollment -> {
                    LocalDateTime deadline = enrollment.getDeadline();
                    if (deadline != null && deadline.isBefore(LocalDateTime.now()))
                        taskScheduler.schedule(() -> System.out.println("Here goes the task"), deadline.toInstant(ZoneOffset.UTC));

                    else if (deadline != null) {
                        // TODO call task "close enrollment"
                    }

                });
    }
}


