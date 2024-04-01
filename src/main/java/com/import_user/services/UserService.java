package com.import_user.services;

import com.import_user.entity.Position;
import com.import_user.entity.User;
import com.import_user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public synchronized User getById(long id){
        User user = userRepository.findById(id);
        if (user == null){
            user = new User();
        }
        return user;
    }

    public synchronized User getSave(User user){
        userRepository.save(user);
        return user;

    }
}
