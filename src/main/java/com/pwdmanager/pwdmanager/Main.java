package com.pwdmanager.pwdmanager;

import com.pwdmanager.pwdmanager.dao.PasswordDAO;
import com.pwdmanager.pwdmanager.model.PasswordEntry;
import com.pwdmanager.pwdmanager.model.User;
import com.pwdmanager.pwdmanager.service.AuthService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        PasswordDAO passwordDao = new PasswordDAO();

        // Registrar usuário
        authService.registerUser("teste123@email.com", "senhaSegura123");

        // Tentar login
        User user = authService.login("teste123@email.com", "senhaSegura123");
        if (user == null) {
            System.out.println("Falha no login. Encerrando.");
            return;
        }

        // Criar e salvar entrada de senha
        PasswordEntry entry = new PasswordEntry();
        entry.setUserId(user.getId());
        entry.setTitle("Minha Conta de Email");
        entry.setPassword("senhaSuperSecreta123");
        passwordDao.save(entry);
        System.out.println("Entrada de senha salva com ID: " + entry.getId());

        // Buscar entradas de senha do usuário
        List<PasswordEntry> entries = passwordDao.findAllByUserId(user.getId());
        System.out.println("Entradas de senha para o usuário:");
        for (PasswordEntry e : entries) {
            System.out.println(e.getId() + ": " + e.getTitle() + " - " + e.getPassword());
        }

        // Atualizar entrada
        entry.setTitle("Conta Email Atualizada");
        entry.setPassword("novaSenhaUltraSecreta456");
        passwordDao.update(entry);
        System.out.println("Entrada de senha atualizada.");

        // Buscar novamente após atualização
        PasswordEntry atualizada = passwordDao.findById(entry.getId());
        System.out.println("Atualizada: " + atualizada.getTitle() + " - " + atualizada.getPassword());

        // Deletar entrada
        passwordDao.delete(entry.getId());
        System.out.println("Entrada de senha deletada.");

        // Deletar usuário
        new com.pwdmanager.pwdmanager.dao.UserDAO().delete(user.getId());
    }
}
