package majorsacasa.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Request {

    int idRequest;
    int idService;
    String state;
    String comments;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dateRequest;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dateAccept;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dateReject;

    public Request() {

    }

    public int getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(int idRequest) {
        this.idRequest = idRequest;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
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

    @Override
    public String toString() {
        return "Request{" +
                "idRequest=" + idRequest +
                ", idService=" + idService +
                ", state='" + state + '\'' +
                ", comments='" + comments + '\'' +
                ", dateRequest=" + dateRequest +
                ", dateAccept=" + dateAccept +
                ", dateReject=" + dateReject +
                '}';
    }
}
