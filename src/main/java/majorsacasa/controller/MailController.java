package majorsacasa.controller;

public class MailController {

    private String from = "cas@gva.es";
    private String to;
    private String subject;
    private String message;

    public MailController(String to) {
        this.to = to;
        System.out.println("Enviando correo...");
    }

    public MailController(String to, String subject, String description) {
        this.to = to;
        this.subject = subject;
        this.message = description;
        System.out.println("Enviando correo...");
    }

    public void sendMessage() {
        String correo = "De: " + from + '\'' +
                ", Para: " + to + '\'' +
                ", Asunto: " + subject + '\'' +
                ", Mensaje: " + message;
        System.out.println(correo);
        System.out.println("Correo enviado");
    }

    public void updateMail(String message) {
        subject = "Actualización";
        String correo = "De: " + from + '\'' +
                ", Para: " + to + '\'' +
                ", Asunto: " + subject + '\'' +
                ", Mensaje: " + message;
        System.out.println(correo);
        System.out.println("Correo enviado");
    }

    public void addMail(String message) {
        subject = "Nuevo registro";
        String correo = "De: " + from + '\'' +
                ", Para: " + to + '\'' +
                ", Asunto: " + subject + '\'' +
                ", Mensaje: " + message;
        System.out.println(correo);
        System.out.println("Correo enviado");
    }

    public void deleteMail(String message) {
        subject = "Borrado";
        String correo = "De: " + from + '\'' +
                ", Para: " + to + '\'' +
                ", Asunto: " + subject + '\'' +
                ", Mensaje: " + message;
        System.out.println(correo);
        System.out.println("Correo enviado");
    }
}
