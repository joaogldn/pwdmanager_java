package com.pwdmanager.pwdmanager.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;

public class TwoFactorAuthService {
    private static final String SENDGRID_API_KEY = System.getenv("SENDGRID_API_KEY");
    private static final String FROM_EMAIL = "gerenciadorsenhas87@gmail.com";

    public void enviarToken(String userEmail, String token) throws IOException {
        if (SENDGRID_API_KEY == null) {
            throw new IllegalStateException("Chave SendGrid não configurada");
        }

        Email from = new Email(FROM_EMAIL);
        Email to = new Email(userEmail);
        String subject = "Seu código de verificação";
        Content content = new Content("text/plain", 
            "Seu código de verificação é: " + token + "\nVálido por 5 minutos.");

        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
                throw new IOException("Erro ao enviar e-mail: " + response.getBody());
            }
        } catch (IOException ex) {
            System.err.println("Erro SendGrid: " + ex.getMessage());
            throw ex;
        }
    }
}