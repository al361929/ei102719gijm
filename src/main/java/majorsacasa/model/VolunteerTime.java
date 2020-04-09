package majorsacasa.model;

import java.time.LocalTime;

public class VolunteerTime {

    String dniVolunteer;
    String dniElderly;
    String mes;
    int dia;
    LocalTime startTime;
    LocalTime endTime;
    String availability;

    public VolunteerTime() {

    }

    public String getDniVolunteer() {
        return dniVolunteer;
    }

    public void setDniVolunteer(String dniVolunteer) {
        this.dniVolunteer = dniVolunteer;
    }

    public String getDniElderly() {
        return dniElderly;
    }

    public void setDniElderly(String dniElderly) {
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "VolunteerTime{" +
                "dniVolunteer='" + dniVolunteer + '\'' +
                ", dniElderly='" + dniElderly + '\'' +
                ", mes='" + mes + '\'' +
                ", dia=" + dia +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", availability='" + availability + '\'' +
                '}';
    }
}
