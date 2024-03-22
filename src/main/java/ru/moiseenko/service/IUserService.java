package ru.moiseenko.service;

import org.springframework.stereotype.Service;
import ru.moiseenko.entity.Notification;
import ru.moiseenko.entity.User;
import java.util.List;

@Service
public interface IUserService {
    boolean updateUser(User user);
    List<Notification> allNotifications(int userId);
    User findUserByName(String username);
    List<User> allUsersNotMe(String username);
}
