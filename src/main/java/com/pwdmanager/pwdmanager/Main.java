package com.pwdmanager.pwdmanager;

import com.pwdmanager.pwdmanager.dao.PasswordDAO;
import com.pwdmanager.pwdmanager.dao.UserDAO;
import com.pwdmanager.pwdmanager.model.PasswordEntry;
import com.pwdmanager.pwdmanager.model.User;
import com.pwdmanager.pwdmanager.service.AuthService;
import com.pwdmanager.pwdmanager.service.PasswordBreachService;
import com.pwdmanager.pwdmanager.service.TokenService;
import com.pwdmanager.pwdmanager.service.TwoFactorAuthService;
import com.pwdmanager.pwdmanager.util.CryptoUtils;
import com.pwdmanager.pwdmanager.util.HashUtils;
import com.pwdmanager.pwdmanager.util.PasswordGenerator;
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
        PasswordBreachService breachService = new PasswordBreachService();
        Scanner scanner = new Scanner(System.in);

        // Verificação de variável de ambiente
        if (System.getenv("SENDGRID_API_KEY") == null) {
            System.out.println("⚠️ AVISO: Variável de ambiente SENDGRID_API_KEY não está configurada.");
        }

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
                    System.out.print("Senha (deixe em branco para gerar uma senha forte): ");
                    String senhaCadastro = scanner.nextLine();

                    // Se o usuário não informou senha, gerar uma forte
                    if (senhaCadastro.isEmpty()) {
                        senhaCadastro = PasswordGenerator.generateSecurePassword();
                        System.out.println("\n🔐 Senha gerada automaticamente: " + senhaCadastro);
                        System.out.println("⚠️ Anote esta senha em um local seguro!\n");
                    }

                    String salt = SaltUtils.getRandomSalt();
                    String hashedPassword = HashUtils.hashPassword(senhaCadastro, salt);

                    User novoUsuario = new User();
                    novoUsuario.setName(nome);
                    novoUsuario.setEmail(email);
                    novoUsuario.setSalt(salt);
                    novoUsuario.setHashedPassword(hashedPassword);

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
                    System.out.println("[DEBUG] Usuário encontrado: " + (usuario != null));

                    if (usuario != null) {
                        try {
                            String token = TokenService.gerarToken();
                            try {
                                authService.enviarToken(loginEmail, token);
                                System.out.println("📨 Código 2FA enviado para o e-mail.");
                            } catch (IOException e) {
                                System.out.println("⚠️ Não foi possível enviar o e-mail. Seu código 2FA é: " + token);
                            }

                            System.out.print("Digite o código 2FA: ");
                            String codigoInput = scanner.nextLine();

                            if (TokenService.validarToken(codigoInput)) {
                                usuarioLogado = usuario;
                                System.out.println("✅ Login realizado com sucesso!");
                            } else {
                                System.out.println("❌ Código 2FA inválido. Login negado.");
                            }

                        } catch (Exception e) {
                            System.out.println("⚠️ Erro no processo de autenticação: " + e.getMessage());
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

                    System.out.print("Senha (deixe em branco para gerar uma senha forte): ");
                    String plainPassword = scanner.nextLine();
                    
                    // Se o usuário não informou senha, gerar uma forte
                    if (plainPassword.isEmpty()) {
                        plainPassword = PasswordGenerator.generateSecurePassword();
                        System.out.println("\n🔐 Senha gerada automaticamente: " + plainPassword);
                    }
                    
                    // Verificar se a senha já foi vazada
                    try {
                        int breachCount = breachService.checkPasswordBreach(plainPassword);
                        if (breachCount > 0) {
                            System.out.println("\n⚠️ ATENÇÃO: Esta senha foi vazada " + breachCount + " vezes!");
                            System.out.println("Recomendamos fortemente que você escolha outra senha.");
                        } else {
                            System.out.println("\n✅ Esta senha não foi encontrada em vazamentos conhecidos.");
                        }
                    } catch (IOException e) {
                        System.out.println("\n⚠️ Não foi possível verificar vazamentos. Erro: " + e.getMessage());
                    }

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
                        System.out.println("⚠️ Não foi possível enviar o e-mail. Seu código 2FA é: " + token);
                    }

                    System.out.print("Digite o código 2FA: ");
                    String codigoInput = scanner.nextLine();

                    if (!TokenService.validarToken(codigoInput)) {
                        System.out.println("❌ Código 2FA inválido. Acesso negado.");
                        break;
                    }

                    PasswordEntry senha = passwordDAO.findById(idSenha);
                    if (senha == null || senha.getUserId() != usuarioLogado.getId()) {
                        System.out.println("Senha não encontrada ou não pertence a você.");
                    } else {
                        String decrypted = CryptoUtils.decrypt(senha.getPassword());
                        System.out.println("🔐 Senha para " + senha.getTitle() + " (" + senha.getUsername() + "): " + decrypted);
                        
                        // Verificar se a senha já foi vazada ao visualizar
                        try {
                            int breachCount = breachService.checkPasswordBreach(decrypted);
                            if (breachCount > 0) {
                                System.out.println("\n⚠️ ATENÇÃO: Esta senha foi vazada " + breachCount + " vezes!");
                                System.out.println("Recomendamos fortemente que você altere esta senha.");
                            } else {
                                System.out.println("\n✅ Esta senha não foi encontrada em vazamentos conhecidos.");
                            }
                        } catch (IOException e) {
                            System.out.println("\n⚠️ Não foi possível verificar vazamentos. Erro: " + e.getMessage());
                        }
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