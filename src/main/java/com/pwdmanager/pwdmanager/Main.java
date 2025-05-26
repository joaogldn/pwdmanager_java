package com.pwdmanager.pwdmanager;

import com.pwdmanager.pwdmanager.dao.PasswordDAO;
import com.pwdmanager.pwdmanager.model.PasswordEntry;
import com.pwdmanager.pwdmanager.model.User;
import com.pwdmanager.pwdmanager.service.AuthService;
import com.pwdmanager.pwdmanager.service.TwoFactorAuthService;
import java.io.IOException;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TwoFactorAuthService authService = new TwoFactorAuthService();
        String emailParaTeste = "joaogald07@gmail.com";  // substitua pelo seu e-mail pessoal
        String token = "123456";  // token de exemplo para teste

        try {
            authService.enviarToken(emailParaTeste, token);
            System.out.println("E-mail enviado com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}
