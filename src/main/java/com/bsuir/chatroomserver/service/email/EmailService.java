package com.bsuir.chatroomserver.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(String email, String token, String username) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String htmlBody = this.createConfirmationEmailBody(username, token);

        helper.setTo(email);
        helper.setSubject("Подтверждение почты");
        helper.setText(htmlBody, true);
        mailSender.send(message);

    }

    private String createConfirmationEmailBody(String name, String token) {
        String confirmationUrl = "http://localhost:8080/auth/confirm?token=" + token;

        return "<html>"
                + "<head>"
                + "<style>"
                + "body {"
                + "  font-family: Arial, sans-serif;"
                + "  background-color: #f2f2f2;"
                + "  margin: 0;"
                + "  padding: 0;"
                + "}"
                + "h1 {"
                + "  color: #333333;"
                + "}"
                + ".button-container {"
                + "  text-align: center;"
                + "  margin-top: 20px;"
                + "}"
                + ".button {"
                + "  display: inline-block;"
                + "  background-color: #A75214;"
                + "  color: white !important;"
                + "  padding: 12px 20px;"
                + "  text-align: center;"
                + "  text-decoration: none;"
                + "  font-size: 16px;"
                + "  border-radius: 5px;"
                + "  transition: background-color 0.3s ease;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<h1>Добро пожаловать, " + name + "!</h1>"
                + "<p>Подтвердите свою почту, нажав на кнопку ниже:</p>"
                + "<div class=\"button-container\">"
                + "  <a href=\"" + confirmationUrl + "\" class=\"button\">Подтвердить почту</a>"
                + "</div>"
                + "<p>Если кнопка не работает, вы также можете скопировать и вставить следующую ссылку в адресную строку браузера:</p>"
                + "<p>" + confirmationUrl + "</p>"
                + "</body>"
                + "</html>";
    }
}
