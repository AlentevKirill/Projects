package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.EventRepository;
import ru.itmo.wp.model.repository.TalkRepository;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.EventRepositoryImpl;
import ru.itmo.wp.model.repository.impl.TalkRepositoryImpl;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;
import ru.itmo.wp.web.exception.RedirectException;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserService {
    private static final String PASSWORD_SALT = "1174f9d7bc21e00e9a5fd0a783a44c9a9f73413c";

    private final UserRepository userRepository = new UserRepositoryImpl();
    private final EventRepository eventRepository = new EventRepositoryImpl();
    private final TalkRepository talkRepository = new TalkRepositoryImpl();

    public void validateRegistration(User user, String password, String passwordConfirm) throws ValidationException {
        if (Strings.isNullOrEmpty(user.getLogin())) {
            throw new ValidationException("Login is required");
        }
        if (!user.getLogin().matches("[a-z]+")) {
            throw new ValidationException("Login can contain only lowercase Latin letters");
        }
        if (user.getLogin().length() > 8) {
            throw new ValidationException("Login can't be longer than 8 letters");
        }
        if (userRepository.findByLoginOrEmail(user.getLogin(), "login") != null) {
            throw new ValidationException("Login is already in use");
        }

        if (Strings.isNullOrEmpty(user.getEmail())) {
            throw new ValidationException("Email is required");
        }
        String email = user.getEmail();
        // [^@]*@[^@]*
        if (!email.contains("@") || email.indexOf('@') != email.lastIndexOf('@')) {
            throw new ValidationException("Email entered incorrectly");
        }
        if (userRepository.findByLoginOrEmail(user.getEmail(), "email") != null) {
            throw new ValidationException("Email is already in use");
        }

        if (Strings.isNullOrEmpty(password)) {
            throw new ValidationException("Password is required");
        }
        if (password.length() < 4) {
            throw new ValidationException("Password can't be shorter than 4 characters");
        }
        if (password.length() > 64) {
            throw new ValidationException("Password can't be longer than 64 characters");
        }

        if (Strings.isNullOrEmpty(passwordConfirm)) {
            throw new ValidationException("Password confirm is required");
        }
        if (passwordConfirm.length() > 64) {
            throw new ValidationException("PasswordConfirm can't be longer than 64 characters");
        }
        if (!passwordConfirm.equals(password)) {
            throw new ValidationException("The confirmation password differs from the one entered earlier.");
        }
    }

    public void register(User user, String password) {
        userRepository.save(user, getPasswordSha(password));
    }
    public void saveEvent(Event event) { eventRepository.save(event); }
    public void saveTalk(Talk talk) { talkRepository.save(talk); }

    private String getPasswordSha(String password) {
        return Hashing.sha256().hashBytes((PASSWORD_SALT + password).getBytes(StandardCharsets.UTF_8)).toString();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public long findCount() {return userRepository.findCount();}

    public void validateEnter(String loginOrEmail, String password) throws ValidationException {
        if (Strings.isNullOrEmpty(loginOrEmail)) {
            throw new ValidationException("Login/email is required");
        }
        if (loginOrEmail.length() > 254) {
            throw new ValidationException("Login/email can't be longer than 254 letters");
        }

        if (Strings.isNullOrEmpty(password)) {
            throw new ValidationException("Password is required");
        }
        if (password.length() < 4) {
            throw new ValidationException("Password can't be shorter than 4 characters");
        }
        if (password.length() > 64) {
            throw new ValidationException("Password can't be longer than 64 characters");
        }

        User user = userRepository.findByLoginOrEmailAndPasswordSha(loginOrEmail, "login", getPasswordSha(password));
        if (user == null) {
            user = userRepository.findByLoginOrEmailAndPasswordSha(loginOrEmail, "email", getPasswordSha(password));
        }
        if (user == null) {
            throw new ValidationException("Invalid login/email or password");
        }
    }

    public void validateTalk(String talkTargetUserLogin, String talkText) throws ValidationException {
        if (Strings.isNullOrEmpty(talkTargetUserLogin)) {
            throw new RedirectException("/talks");
            //throw new ValidationException("Target user id is required");
        }

//        if (talkTargetUserLogin.length() > 8) {
//            throw new RedirectException("/talks");
//            //throw new ValidationException("Login/email can't be longer than 254 letters");
//        }

        if (Strings.isNullOrEmpty(talkText.trim())) {
            throw new RedirectException("/talks");
            //throw new ValidationException("text is required");
        }
        if (talkText.trim().length() > 254) {
            throw new RedirectException("/talks");
            //throw new ValidationException("text can't be longer than 254 characters");
        }
    }

    public User findByLoginAndPassword(String loginOrEmail, String password) {
        User user = userRepository.findByLoginOrEmailAndPasswordSha(loginOrEmail, "login", getPasswordSha(password));
        if (user == null) {
            return userRepository.findByLoginOrEmailAndPasswordSha(loginOrEmail, "email", getPasswordSha(password));
        } else {
            return user;
        }
    }

    public List<Talk> findAllMessages(long userId) { return talkRepository.findAllMessages(userId); }

    public User findByLoginOrEmail(String loginOrEmail, String type) {
        return userRepository.findByLoginOrEmail(loginOrEmail, type);
    }
}
