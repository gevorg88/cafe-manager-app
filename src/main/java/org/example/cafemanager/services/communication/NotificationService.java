package org.example.cafemanager.services.communication;

import org.example.cafemanager.dto.user.CreateUserRequest;

public interface NotificationService {

    /**
     * Send notification to user
     * 
     * @param user
     */
    void notify(CreateUserRequest user);
}
