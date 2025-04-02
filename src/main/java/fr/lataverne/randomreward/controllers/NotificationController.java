package fr.lataverne.randomreward.controllers;

import static fr.lataverne.randomreward.api.RequestPost.sendNotificationVotePost;

public class NotificationController {


    public void sendNotification(String args) {
        try {
            sendNotificationVotePost("MonPseudo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
