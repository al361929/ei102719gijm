package majorsacasa.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;


@Configuration
public class MailConfiguration {

    public static final String EMAIL_TEMPLATE_ENCODING = "UTF-8";

    @Bean
    public TemplateEngine emailTemplateEngine() {
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();

        // Resolver for HTML emails (except the editable one)
        springTemplateEngine.addTemplateResolver(htmlTemplateResolver());

        return springTemplateEngine;
    }

    private ITemplateResolver htmlTemplateResolver() {
        //ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(2));
        //templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
        //templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}
