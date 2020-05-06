package majorsacasa.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Request {

    Integer idRequest;
    Integer idService;
    String dni;
    String nif;
    String state;
    String comments;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dateRequest;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dateAccept;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dateReject;
    String dias;

    public Request() {

    }

    public Integer getNumDias() {
        String dias = getDias();
        String[] vDias = dias.split(",");
        return vDias.length;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public Integer getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(Integer idRequest) {
        this.idRequest = idRequest;
    }

    public Integer getIdService() {
        return idService;
    }

    public void setIdService(Integer idService) {
        this.idService = idService;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDate getDateAccept() {
        return dateAccept;
    }

    public void setDateAccept(LocalDate dateAccept) {
        this.dateAccept = dateAccept;
    }

    public LocalDate getDateReject() {
        return dateReject;
    }

    public void setDateReject(LocalDate dateReject) {
        this.dateReject = dateReject;
    }

    public LocalDate getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(LocalDate dateRequest) {
        this.dateRequest = dateRequest;
    }
    public boolean isCancelada(){
        return state.equals("Cancelada");
    }
    @Override
    public String toString() {
        return "Request{" +
                "idRequest=" + idRequest +
                ", idService=" + idService +
                ", dni='" + dni + '\'' +
                ", nif='" + nif + '\'' +
                ", state='" + state + '\'' +
                ", comments='" + comments + '\'' +
                ", dateRequest=" + dateRequest +
                ", dateAccept=" + dateAccept +
                ", dateReject=" + dateReject +
                ", dias=" + dias +

                '}';
    }
}
