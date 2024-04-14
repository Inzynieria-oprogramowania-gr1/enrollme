package com.company.project.schedulers;

import java.time.LocalDateTime;

import com.company.project.controllers.EnrollmentController;

class DeadlineHandler implements Runnable {
    EnrollmentController controller;
    
    public DeadlineHandler( EnrollmentController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        try{
            controller.emailSending();
        }
        catch (Exception e){
            System.out.println("DeadlineHandler Exception:" + e.getMessage() + " " + LocalDateTime.now().toString());
        }
        
    }
}
