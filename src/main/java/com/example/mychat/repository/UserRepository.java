package com.example.mychat.repository;

import com.example.mychat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findUserById(Long id);

    @RestResource(path = "showInfo")
    @Query("select u from User u inner join u.rooms rooms where rooms.id = ?1")
    List<User> findByRooms_Id(Long id);


}