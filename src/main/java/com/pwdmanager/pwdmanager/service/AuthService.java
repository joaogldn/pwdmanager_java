package com.pwdmanager.pwdmanager.service;

import com.pwdmanager.pwdmanager.dao.UserDAO;
import com.pwdmanager.pwdmanager.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private UserDAO userDao = new UserDAO();

    public void registerUser(String email, String plainPassword) {
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        User user = new User();
        user.setEmail(email);
        user.setHashedPassword(hashed);
        userDao.save(user);
        System.out.println("Usuário registrado com ID: " + user.getId());
    }

    public User login(String email, String plainPassword) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            System.out.println("Usuário não encontrado.");
            return null;
        }
        if (BCrypt.checkpw(plainPassword, user.getHashedPassword())) {
            System.out.println("Login bem-sucedido.");
            return user;
        } else {
            System.out.println("Senha incorreta.");
            return null;
        }
    }
}
