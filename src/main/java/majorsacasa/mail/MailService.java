package majorsacasa.mail;

import majorsacasa.model.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collections;

@Service
public class MailService {

    public static final String EMAIL_TEMPLATE_ENCODING = "UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
    private static final String linea = "src/main/resources/static/img/linea-roja.png";
    private static final String logo = "src/main/resources/static/img/logo.png";

    @Autowired
    JavaMailSender sender;
    TemplateEngine templateEngine;

    @Bean
    public TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        // Resolver for HTML emails (except the editable one)
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        this.templateEngine = templateEngine;
        return templateEngine;
    }

    private ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(2));
        templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
        //templateResolver.setPrefix("classpath:/templates/");
        //templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    public boolean sendEmail(MailBody mailBody, UserDetails user) {
        LOGGER.info("EmailBody: {}", mailBody.toString());
        boolean send = false;
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        final Context context = new Context();

        try {
            context.setVariable("usuario", user);
            context.setVariable("lineaRoja", linea);
            context.setVariable("logo", logo);
            context.setVariable("mailBody", mailBody);
            FileSystemResource file = new FileSystemResource("src/main/resources/templates/email.html");
            System.out.println(file.exists());
            System.out.println(file.getPath());
            final String htmlcontent = templateEngine.process(file.getPath(), context);

            helper.setTo(mailBody.getEmail());
            helper.setText(htmlcontent, true);
            helper.setSubject(mailBody.getSubject());

            sender.send(message);
            send = true;
            LOGGER.info("Mail enviado!");
        } catch (MessagingException e) {
            LOGGER.error("Hubo un error al enviar el mail: {}", e);
        }
        return send;
    }

}