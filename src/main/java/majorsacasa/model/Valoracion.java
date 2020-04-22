package majorsacasa.model;

import java.time.LocalDate;


public class Valoracion {
    String dniVolunteer;
    String dni;
    String comments;
    Integer valoration;
    LocalDate dateValoration;

    public String getDniVolunteer() {
        return dniVolunteer;
    }

    public void setDniVolunteer(String dniVolunteer) {
        this.dniVolunteer = dniVolunteer;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getValoration() {
        return valoration;
    }

    public void setValoration(Integer valoration) {
        this.valoration = valoration;
    }

    public LocalDate getDateValoration() {
        return dateValoration;
    }

    public void setDateValoration(LocalDate dateValoration) {
        this.dateValoration = dateValoration;
    }

    @Override
    public String toString() {
        return "Valoracion{" +
                "dniVolunteer='" + dniVolunteer + '\'' +
                ", dni='" + dni + '\'' +
                ", comments='" + comments + '\'' +
                ", valoration=" + valoration +
                ", dateValoration=" + dateValoration +
                '}';
    }
}
