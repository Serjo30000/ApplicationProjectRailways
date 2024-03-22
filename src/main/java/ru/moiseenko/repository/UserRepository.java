package ru.moiseenko.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.moiseenko.entity.*;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
    @Query("SELECT * FROM USERS WHERE username = :userName")
    User findByUsername(@Param("userName")String username);
    @Query("SELECT * FROM USERS WHERE reg_number = :regNumber")
    User findByRegNumber(@Param("regNumber")int regNumber);
    @Query("SELECT * FROM Users WHERE Users.username!=:username")
    List<User> findUserWithNotMe(@Param("username")String username);
    @Query("SELECT Roles.id,Roles.name FROM Roles JOIN users_roles ON Roles.id = users_roles.roles_id JOIN Users ON" +
            " Users.id = users_roles.user_id WHERE Users.id=:id")
    List<Role> findUserWithRole(@Param("id")int userId);
    @Query("SELECT Notifications.id, Notifications.text_notification, Notifications.id_users, Notifications.recipient" +
            " FROM Notifications JOIN Users ON Notifications.id_users=Users.id WHERE Notifications.id_users=:id")
    List<Notification> findNotificationByAll(@Param("id")int userId);
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id," +
            " Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user," +
            " Orders.patronymic_user, Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders JOIN Users ON" +
            " Orders.userOr_id=Users.id WHERE Orders.userOr_id=:id")
    List<Order> findOrderByAll(@Param("id")int userId);
    @Modifying
    @Query("UPDATE Users SET Users.username=:username, Users.password=:password, Users.phone=:phone, Users.email=:email," +
            " Users.founder=:founder, Users.reg_number=:regNumber, Users.location=:location WHERE Users.id=:id")
    void updateUser(@Param("id")int id,@Param("username")String username,@Param("password")String password,
                    @Param("phone")String phone,@Param("email")String email,@Param("founder")String founder,
                    @Param("regNumber")int regNumber,@Param("location")String location);
}
