package com.pwdmanager.pwdmanager.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;

public class TwoFactorAuthService {
    private static final String SENDGRID_API_KEY = "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"; 
    private static final String FROM_EMAIL = "gerenciadorsenhas87@gmail.com"; 

    public void enviarToken(String emailDestino, String token) throws IOException {
        Email from = new Email(FROM_EMAIL);
        Email to = new Email(emailDestino);
        String subject = "Seu código de verificação PWD Manager";
        Content content = new Content("text/plain", "Seu código de verificação é: " + token + "\n\nCódigo válido por 5 minutos.");
        
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();
        
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            // Adicione logging para debug
            System.out.println("Tentando enviar email para: " + emailDestino);
            System.out.println("Usando API Key: " + SENDGRID_API_KEY.substring(0, 5) + "..."); // Mostra apenas parte da chave por segurança
            
            Response response = sg.api(request);
            
            // Log detalhado da resposta
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());
            System.out.println("Response headers: " + response.getHeaders());
            
            if (response.getStatusCode() >= 400) {
                throw new IOException("Falha no envio: " + response.getBody());
            }
            
        } catch (IOException ex) {
            System.err.println("ERRO no envio do email: " + ex.getMessage());
            throw ex;
        }
    }
}