package majorsacasa.model;

import java.util.Date;

public class Request {

    int id;
    String dni;
    String nif;
    String state;
    String serviceType;
    String comments;
    Date dateRequest;
    Date dateAccept;
    Date dateReject;

    public Request() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getDateAccept() {
        return dateAccept;
    }

    public void setDateAccept(Date dateAccept) {
        this.dateAccept = dateAccept;
    }

    public Date getDateReject() {
        return dateReject;
    }

    public void setDateReject(Date dateReject) {
        this.dateReject = dateReject;
    }

    public Date getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(Date dateRequest) {
        this.dateRequest = dateRequest;
    }

    @Override
    public String toString() {
        return "Request{" +
                "dni='" + dni + '\'' +
                ", nif='" + nif + '\'' +
                ", state='" + state + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", comments='" + comments + '\'' +
                ", dateRequest=" + dateRequest +
                ", dateAccept=" + dateAccept +
                ", dateReject=" + dateReject +
                '}';
    }

}
