package com.pwdmanager.pwdmanager.service;

import com.pwdmanager.pwdmanager.dao.UserDAO;
import com.pwdmanager.pwdmanager.model.User;
import com.pwdmanager.pwdmanager.util.HashUtils;
import com.pwdmanager.pwdmanager.util.SaltUtils;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private UserDAO userDao = new UserDAO();

    public boolean registerUser(String name, String email, String plainPassword) {
        try {
            String salt = SaltUtils.getRandomSalt();
            String hashed = HashUtils.hashPassword(plainPassword, salt);
    
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setSalt(salt);
            user.setHashedPassword(hashed);
    
            boolean saved = userDao.save(user);
            if (saved) {
                System.out.println("Usuário cadastrado com sucesso.");
            } else {
                System.out.println("Falha ao salvar usuário.");
            }
            return saved;
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            return false;
        }
    }
    
    public User login(String email, String plainPassword) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            System.out.println("Usuário não encontrado.");
            return null;
        }
    
        String salt = user.getSalt();
        String hashedInput = HashUtils.hashPassword(plainPassword, salt);
    
        System.out.println("Hash armazenado: " + user.getHashedPassword());
        System.out.println("Hash gerado: " + hashedInput);
    
        if (hashedInput.equals(user.getHashedPassword())) {
            System.out.println("Login bem-sucedido.");
            return user;
        } else {
            System.out.println("Senha incorreta.");
            return null;
        }
    }
}