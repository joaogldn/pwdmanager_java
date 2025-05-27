package com.pwdmanager.pwdmanager.service;

import com.pwdmanager.pwdmanager.dao.UserDAO;
import com.pwdmanager.pwdmanager.model.User;
import com.pwdmanager.pwdmanager.util.HashUtils;
import com.pwdmanager.pwdmanager.util.SaltUtils;
import com.pwdmanager.pwdmanager.util.PasswordGenerator; 

public class AuthService {

    private UserDAO userDao = new UserDAO();

    // Método para gerar senha se não for fornecida
    public boolean registerUser(String name, String email, String plainPassword) {
        try {
            // Se nenhuma senha for fornecida, gera uma segura
            if (plainPassword == null || plainPassword.isEmpty()) {
                plainPassword = PasswordGenerator.generateSecurePassword(14);
                System.out.println("\n🔐 Senha gerada automaticamente: " + plainPassword);
                System.out.println("⚠️ Anote esta senha em um local seguro!\n");
            }

            String salt = SaltUtils.getRandomSalt();
            String hashed = HashUtils.hashPassword(plainPassword, salt);
    
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setSalt(salt);
            user.setHashedPassword(hashed);
    
            boolean saved = userDao.save(user);
            if (saved) {
                System.out.println("✅ Usuário cadastrado com sucesso.");
            } else {
                System.out.println("❌ Falha ao salvar usuário.");
            }
            return saved;
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            return false;
        }
    }

    // Método para apenas gerar senha (uso externo)
    public static String generateStrongPassword() {
        return PasswordGenerator.generateSecurePassword(14);
    }
    
    public User login(String email, String plainPassword) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            System.out.println("Usuário não encontrado.");
            return null;
        }
    
        String salt = user.getSalt();
        String hashedInput = HashUtils.hashPassword(plainPassword, salt);
    
        if (hashedInput.equals(user.getHashedPassword())) {
            System.out.println("✅ Login bem-sucedido.");
            
            // Verificação adicional de força da senha
            if (PasswordGenerator.calculatePasswordStrength(plainPassword) < 4) {
                System.out.println("\n⚠️ Sua senha é fraca! Considere alterá-la usando:");
                System.out.println("Senha sugerida: " + generateStrongPassword());
            }
            
            return user;
        } else {
            System.out.println("❌ Senha incorreta.");
            return null;
        }
    }
}
