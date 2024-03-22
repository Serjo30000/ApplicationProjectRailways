package ru.moiseenko.service;

import org.springframework.stereotype.Service;
import ru.moiseenko.entity.Notification;
import ru.moiseenko.entity.User;
import java.util.List;

@Service
public interface INotificationService {
    boolean updateNotification(Notification notification);
    User findUserByNotificationId(int notificationId);
    List<Notification> allNotificationNames(String name);
    Notification createNotification(String recipient,int userId, String textNotification);
}
