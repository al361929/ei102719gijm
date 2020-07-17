package majorsacasa.mail;

public class MailBody {

    private String from = "majorsacasagva@gmail.com";
    private String email;
    private String subject;
    private String content;

    public MailBody(String email) {
        this.email = email;
    }

    public MailBody(String email, String subject, String description) {
        this.email = email;
        this.subject = subject;
        this.content = description;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void updateMail(String message) {
        setSubject("Actualización");
        setContent(message);
        /*String correo = "De: " + from + '\'' +
                ", Para: " + email + '\'' +
                ", Asunto: " + subject + '\'' +
                ", Mensaje: " + message;*/
    }

    public void addMail(String message) {
        setSubject("Nuevo registro");
        setContent(message);
        /*String correo = "De: " + from + '\'' +
                ", Para: " + email + '\'' +
                ", Asunto: " + subject + '\'' +
                ", Mensaje: " + message;*/
    }

    public void deleteMail(String message) {
        setSubject("Borrado");
        setContent(message);
        /*String correo = "De: " + from + '\'' +
                ", Para: " + email + '\'' +
                ", Asunto: " + subject + '\'' +
                ", Mensaje: " + message;*/
    }
}
