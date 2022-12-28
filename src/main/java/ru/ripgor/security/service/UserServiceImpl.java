package ru.ripgor.security.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ripgor.security.model.User;
import ru.ripgor.security.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User getUser(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User getUser(String name) {
        return userRepository.findByName(name);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        User userToUpdate = userRepository.findById(user.getId());
        userToUpdate.setName(user.getName());
        userToUpdate.setEmail(user.getEmail());

        if (!user.getPassword().equals(userRepository.findById(user.getId()).getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userToUpdate.setRoles(user.getRoles());

        userRepository.save(userToUpdate);
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

}