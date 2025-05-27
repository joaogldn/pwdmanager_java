package com.pwdmanager.pwdmanager;

import com.pwdmanager.pwdmanager.dao.PasswordDAO;
import com.pwdmanager.pwdmanager.dao.UserDAO;
import com.pwdmanager.pwdmanager.model.PasswordEntry;
import com.pwdmanager.pwdmanager.model.User;
import com.pwdmanager.pwdmanager.service.AuthService;
import com.pwdmanager.pwdmanager.service.TokenService;
import com.pwdmanager.pwdmanager.service.TwoFactorAuthService;
import com.pwdmanager.pwdmanager.util.CryptoUtils;
import com.pwdmanager.pwdmanager.util.HashUtils;
import com.pwdmanager.pwdmanager.util.SaltUtils;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TwoFactorAuthService authService = new TwoFactorAuthService();
        AuthService loginService = new AuthService();
        PasswordDAO passwordDAO = new PasswordDAO();
        UserDAO userDAO = new UserDAO();
        Scanner scanner = new Scanner(System.in);

        User usuarioLogado = null;

        while (usuarioLogado == null) {
            System.out.println("\n===== Bem-vindo ao PWD Manager =====");
            System.out.println("1. Cadastrar novo usuário");
            System.out.println("2. Login");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Senha: ");
                    String senhaCadastro = scanner.nextLine();

                    // Correção aplicada aqui: Gerar salt e hash antes de criar o usuário
                    String salt = SaltUtils.getRandomSalt();
                    String hashedPassword = HashUtils.hashPassword(senhaCadastro, salt);

                    User novoUsuario = new User();
                    novoUsuario.setName(nome);
                    novoUsuario.setEmail(email);
                    novoUsuario.setSalt(salt); // Definindo o salt
                    novoUsuario.setHashedPassword(hashedPassword); // Salvando o hash

                    if (userDAO.save(novoUsuario)) {
                        System.out.println("✅ Usuário cadastrado com sucesso!");
                    } else {
                        System.out.println("❌ Erro ao cadastrar usuário.");
                    }
                    break;

                case "2":
                    System.out.print("Email: ");
                    String loginEmail = scanner.nextLine();
                    System.out.print("Senha: ");
                    String loginSenha = scanner.nextLine();

                    User usuario = loginService.login(loginEmail, loginSenha);
                    if (usuario != null) {
                        String token = TokenService.gerarToken();
                        try {
                            authService.enviarToken(loginEmail, token);
                            System.out.println("📨 Código 2FA enviado para o e-mail.");
                        } catch (IOException e) {
                            System.out.println("Erro ao enviar e-mail: " + e.getMessage());
                            break;
                        }

                        System.out.print("Digite o código 2FA: ");
                        String codigoInput = scanner.nextLine();

                        if (!TokenService.validarToken(codigoInput)) {
                            System.out.println("❌ Código 2FA inválido. Login negado.");
                        } else {
                            usuarioLogado = usuario;
                            System.out.println("✅ Login realizado com sucesso!");
                        }
                    } else {
                        System.out.println("❌ Credenciais inválidas.");
                    }
                    break;

                case "3":
                    System.out.println("Saindo...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opção inválida.");
            }
        }

        // Menu principal após login
        while (true) {
            System.out.println("\n===== Gerenciador de Senhas =====");
            System.out.println("1. Cadastrar nova senha");
            System.out.println("2. Listar Senhas");
            System.out.println("3. Visualizar senha (com 2FA)");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    System.out.print("Título (nome do serviço): ");
                    String title = scanner.nextLine();

                    System.out.print("Nome do usuário (login ou e-mail): ");
                    String username = scanner.nextLine();

                    System.out.print("Senha: ");
                    String plainPassword = scanner.nextLine();
                    String encryptedPassword = CryptoUtils.encrypt(plainPassword);

                    PasswordEntry novaSenha = new PasswordEntry(usuarioLogado.getId(), title, username, encryptedPassword, title);
                    passwordDAO.save(novaSenha);
                    System.out.println("✅ Senha cadastrada com sucesso!");
                    break;

                case "2":
                    List<PasswordEntry> lista = passwordDAO.findAllByUserId(usuarioLogado.getId());
                    if (lista.isEmpty()) {
                        System.out.println("Nenhuma senha cadastrada.");
                    } else {
                        System.out.println("\nSenhas Cadastradas:");
                        for (PasswordEntry entry : lista) {
                            System.out.println("ID: " + entry.getId() + " - " + entry.getTitle() + " (" + entry.getUsername() + ")");
                        }
                    }
                    break;

                case "3":
                    System.out.print("Informe o ID da senha que deseja visualizar: ");
                    String idStr = scanner.nextLine();
                    int idSenha;
                    try {
                        idSenha = Integer.parseInt(idStr);
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido.");
                        break;
                    }

                    String token = TokenService.gerarToken();
                    try {
                        authService.enviarToken(usuarioLogado.getEmail(), token);
                        System.out.println("📨 Código 2FA enviado para o e-mail.");
                    } catch (IOException e) {
                        System.out.println("Erro ao enviar e-mail: " + e.getMessage());
                        break;
                    }

                    System.out.print("Digite o código 2FA: ");
                    String codigoInput = scanner.nextLine();

                    if (!TokenService.validarToken(codigoInput)) {
                        System.out.println("❌ Código 2FA inválido. Acesso negado.");
                        break;
                    }

                    PasswordEntry senha = passwordDAO.findById(idSenha);
                    if (senha == null || senha.getUserId() != usuarioLogado.getId()) {
                        System.out.println("Senha não encontrada ou você não se cadastrou a senha dentro do gerenciador.");
                    } else {
                        String decrypted = CryptoUtils.decrypt(senha.getPassword());
                        System.out.println("🔐 Senha para " + senha.getTitle() + " (" + senha.getUsername() + "): " + decrypted);
                    }
                    break;

                case "4":
                    System.out.println("Saindo...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}