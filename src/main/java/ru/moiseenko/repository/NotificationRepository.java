package ru.moiseenko.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.moiseenko.entity.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends CrudRepository<Notification,Integer> {
    @Query("SELECT Users.id, Users.username, Users.password, Users.phone, Users.email, Users.founder, Users.reg_number, " +
        "Users.location FROM Users JOIN Notifications ON Notifications.id_users=Users.id WHERE Notifications.id=:id")
    Optional<User> findUserById(@Param("id")int notificationId);
    @Query("SELECT Users.id, Users.username, Users.password, Users.phone, Users.email, Users.founder, Users.reg_number, " +
        "Users.location FROM Users JOIN Notifications ON Notifications.id_users=Users.id WHERE Notifications.recipient=:name")
    Optional<User> findUserByName(@Param("name")String notificationUserName);
    @Modifying
    @Query("UPDATE Notifications SET Notifications.text_notification=:textNotification, Notifications.id_users=:idUser, " +
        "Notifications.recipient=:recipient WHERE Notifications.id=:id")
    void updateNotification(@Param("id")int id,@Param("textNotification")String textNotification,@Param("idUser")int idUser,@Param("recipient")String recipient);
    @Query("SELECT Notifications.id, Notifications.id_users, Notifications.text_notification, Notifications.recipient" +
        " FROM Notifications WHERE Notifications.recipient=:name")
    List<Notification> findNotificationByName(@Param("name")String name);
}
