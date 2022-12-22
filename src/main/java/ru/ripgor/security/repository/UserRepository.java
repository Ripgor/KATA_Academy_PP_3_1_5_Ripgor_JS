package ru.ripgor.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ripgor.security.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findById(int id);

    @Query("select u from User u join fetch u.roles where u.name = :name")
    User findByName(@Param("name") String name);

    @Query("SELECT u from User u")
    List<User> getAllUsers();
}