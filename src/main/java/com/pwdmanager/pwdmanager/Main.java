package com.pwdmanager.pwdmanager;

import com.pwdmanager.pwdmanager.dao.UserDAO;
import com.pwdmanager.pwdmanager.model.User;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserDAO userDao = new UserDAO();

        // Criar e salvar novo usuário
        User user = new User();
        user.setEmail("teste123@email.com");
        user.setHashedPassword("senhaHashedAqui");  // Simule hash para teste
        userDao.save(user);
        System.out.println("Usuário criado com ID: " + user.getId());

        // Buscar usuário pelo ID
        User userBuscado = userDao.findById(user.getId());
        if (userBuscado != null) {
            System.out.println("Usuário buscado: " + userBuscado.getEmail());

            // Atualizar usuário
            userBuscado.setEmail("testeAtualizado@email.com");
            userDao.update(userBuscado);
            System.out.println("Usuário atualizado.");

        } else {
            System.out.println("Usuário não encontrado para atualizar.");
        }

        // Listar todos usuários
        List<User> usuarios = userDao.findAll();
        System.out.println("Lista de usuários:");
        for (User u : usuarios) {
            System.out.println(u.getId() + ": " + u.getEmail());
        }

        // Deletar usuário
        userDao.delete(user.getId());
        System.out.println("Usuário deletado.");
    }
}
