package com.example.taskmanager.model.customobservers.notificationdecision;

public class NotificationSender {
    private NotificationStrategy notification;

    public void setNotification(NotificationStrategy notification) {
        this.notification = notification;
    }

    public void sendChosenNotification() {
        notification.sendNotification();
    }
}
