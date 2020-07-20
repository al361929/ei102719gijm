package majorsacasa.mail;

import majorsacasa.model.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Configuration
public class MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @Autowired
    JavaMailSender sender;

    @Autowired
    SpringTemplateEngine templateEngine;

    public boolean sendEmail(MailBody mailBody, UserDetails user) {
        LOGGER.info("Enviando correo...");
        boolean send = false;
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            Context context = new Context();
            context.setVariable("usuario", user);
            context.setVariable("mailBody", mailBody);

            final String htmlcontent = this.templateEngine.process("email.html", context);

            helper.setTo(mailBody.getEmail());
            helper.setText(htmlcontent, true);
            helper.setSubject(mailBody.getSubject());

            Resource navEmail = new FileSystemResource(new File(("src/main/resources/static/img/navEmail.png")));
            helper.addInline("navEmail.png", navEmail);


            sender.send(message);
            send = true;
            LOGGER.info("Correo enviado correctamente");
        } catch (MessagingException e) {
            LOGGER.error("Hubo un error al enviar el mail: {}", e);
        }
        return send;
    }

}