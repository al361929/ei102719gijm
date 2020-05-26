package majorsacasa.controller;

public class MailController {

    private String from = "cas@gva.es";
    private String to;
    private String subject;
    private String message;

    public MailController(String to, String subject, String description) {
        this.to = to;
        this.subject = subject;
        this.message = description;
    }

    public void sendMessage() {
        System.out.println("Enviando correo...");
        String correo = "De: " + from + '\'' +
                ", Para: " + to + '\'' +
                ", Asunto: " + subject + '\'' +
                ", Mensaje: " + message;
        System.out.println(correo);
        System.out.println("Correo enviado");
    }

}
