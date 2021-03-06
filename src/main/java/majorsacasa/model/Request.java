package majorsacasa.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;

public class Request implements Comparable<Request> {

    Integer idRequest;
    Integer idService;
    String dni;
    String nif;
    String state;
    String comments;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dateStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dateEnd;
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

    public ArrayList<String> getDiasIngles() {
        String[] dias = getDias().split(",");
        ArrayList<String> diasIngles = new ArrayList<>();
        for (String dia : dias) {
            switch (dia) {
                case "Lunes":
                    diasIngles.add("MONDAY");
                    break;
                case "Martes":
                    diasIngles.add("TUESDAY");
                    break;
                case "Miercoles":
                    diasIngles.add("WEDNESDAY");
                    break;
                case "Jueves":
                    diasIngles.add("THURSDAY");
                    break;
                case "Viernes":
                    diasIngles.add("FRIDAY");
            }
        }
        return diasIngles;
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

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
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

    public boolean isCancelada() {
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
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", dateRequest=" + dateRequest +
                ", dateAccept=" + dateAccept +
                ", dateReject=" + dateReject +
                ", dias='" + dias + '\'' +
                '}';
    }

    @Override
    public int compareTo(Request otro) {
        Boolean after = this.getDateRequest().isBefore(otro.getDateRequest());
        if (after) {
            return 1;
        }
        return -1;
    }
}
