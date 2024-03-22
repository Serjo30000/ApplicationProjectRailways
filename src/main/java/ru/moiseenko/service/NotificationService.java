package ru.moiseenko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moiseenko.entity.Notification;
import ru.moiseenko.entity.User;
import ru.moiseenko.repository.NotificationRepository;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService implements INotificationService{
    @Autowired
    NotificationRepository notificationRepository;
    public Notification findNotificationById(int notificationId){
        Optional<Notification> notificationFromDb = notificationRepository.findById(notificationId);
        return notificationFromDb.orElse(new Notification());
    }
    public List<Notification> allNotifications() {
        return (List<Notification>) notificationRepository.findAll();
    }
    public boolean deleteNotification(int notificationId) {
        if (notificationRepository.findById(notificationId).isPresent()) {
            notificationRepository.deleteById(notificationId);
            return true;
        }
        return false;
    }
    public boolean saveNotification(Notification notification) {
        if (notification == null || notification.getTextNotification().equals("")) {
            return false;
        }
        notificationRepository.save(notification);
        return true;
    }
    @Override
    public boolean updateNotification(Notification notification) {
        if (notification == null || notification.getTextNotification().equals("")) {
            return true;
        }
        Notification notificationToUpdate = notificationRepository.findById(notification.getId()).get();
        notificationToUpdate.setTextNotification(notification.getTextNotification());
        notificationToUpdate.setIdUser(notification.getIdUser());
        notificationToUpdate.setRecipient(notification.getRecipient());
        notificationRepository.updateNotification(notificationToUpdate.getId(),notificationToUpdate.getTextNotification(),
                notificationToUpdate.getIdUser(),notificationToUpdate.getRecipient());
        return false;
    }
    @Override
    public User findUserByNotificationId(int notificationId) {
        Optional<User> userFromDb = notificationRepository.findUserById(notificationId);
        return userFromDb.orElse(new User());
    }
    @Override
    public List<Notification> allNotificationNames(String name){
        return notificationRepository.findNotificationByName(name);
    }
    @Override
    public Notification createNotification(String recipient,int userId, String textNotification){
        if (recipient==null || textNotification==null || userId<=0){
            return null;
        }
        Notification notification = new Notification();
        notification.setIdUser(userId);
        notification.setRecipient(recipient);
        notification.setTextNotification(textNotification);
        return notification;
    }
}
