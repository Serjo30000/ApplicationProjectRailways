package ru.moiseenko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.moiseenko.entity.*;
import ru.moiseenko.repository.RelationRepository;
import ru.moiseenko.repository.RoleRepository;
import ru.moiseenko.repository.UserRepository;
import java.util.*;

@Service
public class UserService implements UserDetailsService,IUserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RelationRepository relationRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        for (int i=0;i<userRepository.findUserWithRole(user.getId()).size();i++){
            user.getRoles().add(userRepository.findUserWithRole(user.getId()).get(i));
        }
        return user;
    }
    @Override
    public User findUserByName(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        for (int i=0;i<userRepository.findUserWithRole(user.getId()).size();i++){
            user.getRoles().add(userRepository.findUserWithRole(user.getId()).get(i));
        }
        return user;
    }

    public User findUserById(int userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        for (int i=0;i<userRepository.findUserWithRole(userFromDb.get().getId()).size();i++){
            userFromDb.get().getRoles().add(userRepository.findUserWithRole(userFromDb.get().getId()).get(i));
        }
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        List<User>lstUser=(List<User>)userRepository.findAll();
        for (int i=0;i<lstUser.size();i++){
            for (int j=0;j<userRepository.findUserWithRole(lstUser.get(i).getId()).size();j++){
                lstUser.get(i).getRoles().add(userRepository.findUserWithRole(lstUser.get(i).getId()).get(j));
            }
        }
        return lstUser;
    }
    @Override
    public List<User> allUsersNotMe(String username) {
        List<User>lstUser=userRepository.findUserWithNotMe(username);
        for (int i=0;i<lstUser.size();i++){
            for (int j=0;j<userRepository.findUserWithRole(lstUser.get(i).getId()).size();j++){
                lstUser.get(i).getRoles().add(userRepository.findUserWithRole(lstUser.get(i).getId()).get(j));
            }
        }
        return lstUser;
    }

    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        int idRole=5;
        if (user.getRegNumber()==0){
            idRole=5;
            user.setRoles(Collections.singleton(new Role(5, "ROLE_CLIENT")));
        }
        else{
            if (userRepository.findByRegNumber(user.getRegNumber())!=null){
                return false;
            }
            idRole=6;
            user.setRoles(Collections.singleton(new Role(6, "ROLE_COMPANY")));
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        Relation relation = new Relation();
        relation.setUserId(userRepository.findByUsername(user.getUsername()).getId());
        relation.setRoleId(idRole);
        relationRepository.save(relation);
        return true;
    }

    public boolean deleteUser(int userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUser(User user) {
        if (user == null) {
            return true;
        }
        User userToUpdate = userRepository.findById(user.getId()).get();
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPhone(user.getPhone());
        userToUpdate.setFounder(user.getFounder());
        userToUpdate.setLocation(user.getLocation());
        userToUpdate.setRegNumber(user.getRegNumber());
        userToUpdate.setPasswordConfirm(user.getPasswordConfirm());
        userToUpdate.setRoles(user.getRoles());
        userRepository.updateUser(userToUpdate.getId(),userToUpdate.getUsername(),userToUpdate.getPassword(),
                userToUpdate.getPhone(),userToUpdate.getEmail(),userToUpdate.getFounder(),userToUpdate.getRegNumber(),
                userToUpdate.getLocation());
        relationRepository.updateRelation(userToUpdate.getId(),userToUpdate.getRoles().iterator().next().getId());
        return false;
    }
    @Override
    public List<Notification> allNotifications(int userId) {
        return userRepository.findNotificationByAll(userId);
    }
}
