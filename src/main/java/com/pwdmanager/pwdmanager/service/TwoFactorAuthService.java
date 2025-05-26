package com.pwdmanager.pwdmanager.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.pwdmanager.pwdmanager.model.TwoFactorToken;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TwoFactorAuthService {

    private static final String SENDGRID_API_KEY = System.getenv("SENDGRID_API_KEY");

    public void enviarToken(String emailDestino, String token) throws IOException {
        Email from = new Email("gerenciadorsenhas87@gmail.com");  // remetente autorizado no SendGrid
        Email to = new Email("joaogald07@gmail.com");                       // destinatário (seu email pessoal)
        String subject = "Seu código 2FA";
        Content content = new Content("text/plain", "Seu código de verificação é: " + token);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("Status da requisição: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
            System.out.println("Headers: " + response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}