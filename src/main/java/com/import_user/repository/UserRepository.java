package com.import_user.repository;

import com.import_user.entity.User;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByName(String lastName);

    User findById(long id);
}
