package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.User;

import java.util.List;

public interface UserRepository {
    User find(long id);

    User findByLoginOrEmail(String loginOrEmail, String type);

    User findByLoginOrEmailAndPasswordSha(String login, String type, String passwordSha);


    List<User> findAll();
    long findCount();

    void save(User user, String passwordSha);
}
